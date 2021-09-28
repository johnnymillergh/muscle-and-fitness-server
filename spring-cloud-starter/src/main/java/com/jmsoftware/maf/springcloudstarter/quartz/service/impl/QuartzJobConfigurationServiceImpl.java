package com.jmsoftware.maf.springcloudstarter.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.validation.ValidationUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import com.jmsoftware.maf.springcloudstarter.quartz.mapper.QuartzJobConfigurationMapper;
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService;
import com.jmsoftware.maf.springcloudstarter.quartz.util.ScheduleUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jmsoftware.maf.springcloudstarter.function.BooleanCheck.requireTrue;

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
    private final MafProjectProperty mafProjectProperty;

    @PostConstruct
    public void initQuartzJob() throws SchedulerException {
        val scheduler = this.schedulerFactoryBean.getScheduler();
        scheduler.clear();
        val jobList = this.getQuartzJobConfigurationForInitialization();
        for (val quartzJobConfiguration : jobList) {
            Optional.ofNullable(ScheduleUtil.createScheduleJob(
                    scheduler,
                    quartzJobConfiguration,
                    this.mafProjectProperty.getProjectArtifactId()
            )).ifPresent(jobDetail -> log.info("Created schedule job. JobKey: {}", jobDetail.getKey()));
        }
    }

    private List<QuartzJobConfiguration> getQuartzJobConfigurationForInitialization() {
        val queryWrapper = Wrappers.lambdaQuery(QuartzJobConfiguration.class);
        queryWrapper.eq(QuartzJobConfiguration::getServiceName, this.mafProjectProperty.getProjectArtifactId())
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
        val quartzJobConfigurationList = beanList.stream().map(quartzJobConfigurationExcel -> {
            val quartzJobConfiguration = new QuartzJobConfiguration();
            BeanUtil.copyProperties(quartzJobConfigurationExcel, quartzJobConfiguration);
            return quartzJobConfiguration;
        }).collect(Collectors.toList());
        requireTrue(
                this.saveBatch(quartzJobConfigurationList),
                saved -> log.info("Saved quartzJobConfigurationList, saved: {}", saved)
        ).orElseThrow(() -> new IllegalStateException("Failed to save batch quartzJobConfigurationList"));
    }

    @Override
    public List<QuartzJobConfigurationExcel> getListForExporting() {
        return this.getBaseMapper().selectListForExporting(this.mafProjectProperty.getProjectArtifactId());
    }
}
