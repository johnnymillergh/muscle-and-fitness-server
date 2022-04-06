package com.jmsoftware.maf.springcloudstarter.quartz.service.impl;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.validation.ValidationUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import com.jmsoftware.maf.springcloudstarter.quartz.converter.QuartzJobConfigurationMapStructMapper;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import com.jmsoftware.maf.springcloudstarter.quartz.repository.QuartzJobConfigurationMapper;
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService;
import com.jmsoftware.maf.springcloudstarter.quartz.util.CronUtil;
import com.jmsoftware.maf.springcloudstarter.quartz.util.ScheduleUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.jmsoftware.maf.springcloudstarter.function.BooleanCheck.requireTrue;
import static com.jmsoftware.maf.springcloudstarter.quartz.constant.ScheduleConstant.QUARTZ_JOB_CONFIGURATION;
import static java.util.Objects.requireNonNull;

/**
 * Description: QuartzJobConfigurationServiceImpl
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobConfigurationServiceImpl
        extends ServiceImpl<QuartzJobConfigurationMapper, QuartzJobConfiguration>
        implements QuartzJobConfigurationService {
    private final SchedulerFactoryBean schedulerFactoryBean;
    private final MafProjectProperties mafProjectProperties;

    @PostConstruct
    public void initQuartzJob() throws SchedulerException {
        val scheduler = this.schedulerFactoryBean.getScheduler();
        scheduler.clear();
        val jobList = this.getQuartzJobConfigurationForInitialization();
        for (val quartzJobConfiguration : jobList) {
            Optional.ofNullable(ScheduleUtil.createScheduleJob(
                    scheduler,
                    quartzJobConfiguration,
                    this.mafProjectProperties.getProjectArtifactId()
            )).ifPresent(jobDetail -> log.info("Created schedule job. JobKey: {}", jobDetail.getKey()));
        }
    }

    private List<QuartzJobConfiguration> getQuartzJobConfigurationForInitialization() {
        val queryWrapper = Wrappers.lambdaQuery(QuartzJobConfiguration.class);
        queryWrapper.eq(QuartzJobConfiguration::getServiceName, this.mafProjectProperties.getProjectArtifactId())
                .orderByAsc(QuartzJobConfiguration::getId);
        return this.list(queryWrapper);
    }

    @Override
    public PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> getPageList(
            @Valid GetQuartzJobConfigurationPageListPayload payload
    ) {
        val page = new Page<GetQuartzJobConfigurationPageListItem>(payload.getCurrentPage(), payload.getPageSize());
        this.getBaseMapper().selectPageList(page, payload);
        return PageResponseBodyBean.ofSuccess(page.getRecords(), page.getTotal());
    }

    @Override
    @SneakyThrows
    public void validateBeforeAddToBeanList(List<QuartzJobConfigurationExcel> beanList,
                                            QuartzJobConfigurationExcel bean, int index) {
        val beanValidationResult = ValidationUtil.warpValidate(bean);
        requireTrue(
                beanValidationResult.isSuccess(),
                success -> log.info("QuartzJobConfigurationExcel validation result: {}", success)
        ).orElseThrow(
                () -> new IllegalStateException("Invalid data. " + beanValidationResult.getErrorMessages().get(0))
        );
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class)
    public void save(@NotEmpty List<@Valid QuartzJobConfigurationExcel> beanList) {
        requireTrue(
                this.saveBatch(
                        beanList.stream()
                                .map(QuartzJobConfigurationMapStructMapper.INSTANCE::of)
                                .toList()
                ),
                saved -> log.info("Saved quartzJobConfigurationList, saved: {}", saved)
        ).orElseThrow(() -> new IllegalStateException("Failed to save batch quartzJobConfigurationList"));
    }

    @Override
    public List<QuartzJobConfigurationExcel> getListForExporting() {
        return this.getBaseMapper().selectListForExporting(this.mafProjectProperties.getProjectArtifactId());
    }

    @Override
    @SneakyThrows
    public Long create(
            @Valid @NotNull CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        this.validateCronExpression(payload.getCronExpression());
        val quartzJobConfiguration = payload.asQuartzJobConfiguration();
        quartzJobConfiguration.setServiceName(this.mafProjectProperties.getProjectArtifactId());
        requireTrue(this.save(quartzJobConfiguration), saved -> log.info("Quartz job configuration saved: {}", saved))
                .orElseThrow(() -> new IllegalStateException("Failed to save quartz job configuration"));
        return quartzJobConfiguration.getId();
    }

    private void validateCronExpression(String cronExpression) throws Throwable {
        requireTrue(
                CronUtil.isValid(cronExpression),
                valid -> log.warn("Cron validation: {}, expression: {}", valid, cronExpression)
        ).orElseThrow(() -> new IllegalArgumentException(format("Cron({}) invalid", cronExpression)));
    }

    @Override
    @SneakyThrows
    public Long modify(
            @NotNull Long id,
            @Valid @NotNull CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        this.validateCronExpression(payload.getCronExpression());
        val quartzJobConfiguration = payload.asQuartzJobConfiguration();
        quartzJobConfiguration.setId(id);
        requireTrue(
                this.updateById(quartzJobConfiguration),
                updated -> log.warn("Quartz job configuration updated: {}", updated)
        ).orElseThrow(() -> new IllegalStateException("Failed to update quartz job configuration"));
        return id;
    }

    @Override
    @SneakyThrows
    public Long patch(
            @NotNull Long id,
            @NotBlank String property,
            @NotNull CreateOrModifyQuartzJobConfigurationPayload payload
    ) {
        val value = ReflectUtil.getFieldValue(payload, property);
        requireNonNull(value, format("Property's value({}) must not be null", property));
        val validationResult = ValidationUtil.warpValidateProperty(payload, property);
        requireTrue(
                validationResult.isSuccess(),
                valid -> log.warn("Quartz job configuration patched: {}", valid)
        ).orElseThrow(() -> new IllegalStateException(format("{} invalid", property)));
        val quartzJobConfiguration = new QuartzJobConfiguration();
        ReflectUtil.setFieldValue(quartzJobConfiguration, property, value);
        quartzJobConfiguration.setId(id);
        requireTrue(
                this.updateById(quartzJobConfiguration),
                updated -> log.warn("Quartz job configuration patched: {}", updated)
        ).orElseThrow(() -> new IllegalStateException(format("Failed to patch {}", property)));
        return id;
    }

    @Override
    @SneakyThrows
    public Long runImmediately(@NotNull Long id) {
        val quartzJobConfiguration = this.getById(id);
        requireNonNull(quartzJobConfiguration, format("Quartz job(id:{}) must not be null", id));
        val scheduler = this.schedulerFactoryBean.getScheduler();
        val jobDataMap = new JobDataMap();
        jobDataMap.put(QUARTZ_JOB_CONFIGURATION, quartzJobConfiguration);
        scheduler.triggerJob(
                ScheduleUtil.getJobKey(
                        quartzJobConfiguration.getId(),
                        quartzJobConfiguration.getGroup(),
                        this.mafProjectProperties.getProjectArtifactId()),
                jobDataMap
        );
        log.warn("Triggered Quartz job successfully, {}", quartzJobConfiguration);
        return id;
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Throwable.class)
    public Long delete(@NotNull Long id, @NotBlank String group) {
        requireTrue(
                this.removeById(id),
                deleted -> log.warn("Quartz job configuration deleted: {}", deleted)
        ).orElseThrow(() -> new IllegalStateException(format("Failed to delete Quartz job configuration")));
        val scheduler = this.schedulerFactoryBean.getScheduler();
        val deletedJob = scheduler.deleteJob(
                ScheduleUtil.getJobKey(id, group, this.mafProjectProperties.getProjectArtifactId())
        );
        requireTrue(
                deletedJob,
                deletedJob1 -> log.warn("Scheduler deleted job and related triggers: {}", deletedJob1)
        ).orElseThrow(() -> new IllegalStateException("Failed to delete job by scheduler"));
        return id;
    }
}
