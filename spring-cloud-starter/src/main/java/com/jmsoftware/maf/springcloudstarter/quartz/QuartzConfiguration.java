package com.jmsoftware.maf.springcloudstarter.quartz;

import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Description: QuartzConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:44 PM
 **/
@Slf4j
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class, PlatformTransactionManager.class})
public class QuartzConfiguration {
    private static final String THREAD_COUNT = "org.quartz.threadPool.threadCount";

    @Bean
    public SchedulerFactoryBeanCustomizer threadPoolCustomizer(QuartzProperties quartzProperties) {
        return schedulerFactoryBean -> {
            val cpuCoreCount = Runtime.getRuntime().availableProcessors();
            val threadCount = String.valueOf(cpuCoreCount * 2);
            quartzProperties.getProperties().put(THREAD_COUNT, threadCount);
            log.warn("Quartz thread pool enhanced by current cpuCoreCount: {}, threadCount: {}", cpuCoreCount,
                     threadCount);
            schedulerFactoryBean.setQuartzProperties(this.asProperties(quartzProperties.getProperties()));
        };
    }

    private Properties asProperties(Map<String, String> source) {
        val properties = new Properties();
        properties.putAll(source);
        return properties;
    }

    @Bean
    public GreetingQuartzJobBean greetingQuartzJobBean(MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: '{}'", GreetingQuartzJobBean.class.getSimpleName());
        return new GreetingQuartzJobBean(mafProjectProperty);
    }

    @Bean("greetingQuartzJobDetailFactoryBean")
    public JobDetailFactoryBean greetingQuartzJobDetailFactoryBean(GreetingQuartzJobBean greetingQuartzJobBean) {
        val jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setName("greeting-job-detail");
        jobDetailFactoryBean.setGroup("default");
        jobDetailFactoryBean.setJobClass(greetingQuartzJobBean.getClass());
        jobDetailFactoryBean.setDescription("Greeting job detail created by JobDetailFactoryBean");
        jobDetailFactoryBean.setDurability(true);
        log.warn("Initial bean: '{}'", JobDetailFactoryBean.class.getSimpleName());
        return jobDetailFactoryBean;
    }

    @Bean("greetingQuartzJobCronTriggerFactoryBean")
    public CronTriggerFactoryBean greetingQuartzJobCronTriggerFactoryBean(
            @Qualifier("greetingQuartzJobDetailFactoryBean") JobDetailFactoryBean jobDetailFactoryBean) {
        val cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setName("greeting-cron-trigger");
        cronTriggerFactoryBean.setGroup("default");
        cronTriggerFactoryBean.setCronExpression("0 0/1 * 1/1 * ? *");
        val optionalJobDetail = Optional.ofNullable(jobDetailFactoryBean.getObject());
        optionalJobDetail.ifPresent(cronTriggerFactoryBean::setJobDetail);
        log.warn("Initial bean: '{}'", CronTriggerFactoryBean.class.getSimpleName());
        return cronTriggerFactoryBean;
    }

    @Bean
    public QuartzJobService quartzJobService(SchedulerFactoryBean schedulerFactoryBean) {
        log.warn("Initial bean: '{}'", QuartzJobServiceImpl.class.getSimpleName());
        return new QuartzJobServiceImpl(schedulerFactoryBean);
    }
}
