package com.jmsoftware.maf.springcloudstarter.quartz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import com.jmsoftware.maf.springcloudstarter.quartz.mapper.QuartzJobConfigurationMapper;
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService;
import com.jmsoftware.maf.springcloudstarter.quartz.util.ScheduleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
}
