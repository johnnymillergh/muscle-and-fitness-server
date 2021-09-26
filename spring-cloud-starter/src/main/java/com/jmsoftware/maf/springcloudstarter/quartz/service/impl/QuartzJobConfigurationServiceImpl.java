package com.jmsoftware.maf.springcloudstarter.quartz.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
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
import java.util.List;

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
        val jobList = this.getQuartzJobConfigurationForService();
        for (val quartzJobConfiguration : jobList) {
            val jobDetail = ScheduleUtil.createScheduleJob(
                    scheduler,
                    quartzJobConfiguration,
                    this.mafProjectProperty.getProjectArtifactId()
            );
            if (jobDetail != null) {
                log.info("Created schedule job. JobKey: {}", jobDetail.getKey());
            }
        }
    }

    private List<QuartzJobConfiguration> getQuartzJobConfigurationForService() {
        val queryWrapper = Wrappers.lambdaQuery(QuartzJobConfiguration.class);
        queryWrapper.eq(QuartzJobConfiguration::getServiceName, this.mafProjectProperty.getProjectArtifactId())
                .orderByAsc(QuartzJobConfiguration::getId);
        return this.list(queryWrapper);
    }
}
