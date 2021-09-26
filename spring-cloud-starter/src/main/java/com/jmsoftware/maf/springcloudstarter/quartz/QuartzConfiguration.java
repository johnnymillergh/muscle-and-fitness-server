package com.jmsoftware.maf.springcloudstarter.quartz;

import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springcloudstarter.quartz.controller.QuartzJobConfigurationController;
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService;
import com.jmsoftware.maf.springcloudstarter.quartz.service.impl.QuartzJobConfigurationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;
import java.util.Properties;

import static org.springframework.scheduling.quartz.SchedulerFactoryBean.PROP_THREAD_COUNT;

/**
 * Description: QuartzConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:44 PM
 **/
@Slf4j
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class, PlatformTransactionManager.class})
public class QuartzConfiguration {
    @Bean
    public SchedulerFactoryBeanCustomizer threadPoolCustomizer(QuartzProperties quartzProperties) {
        return schedulerFactoryBean -> {
            val cpuCoreCount = Runtime.getRuntime().availableProcessors();
            val threadCount = String.valueOf(cpuCoreCount * 2);
            quartzProperties.getProperties().put(PROP_THREAD_COUNT, threadCount);
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
    public GreetingBean greetingBean(MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: '{}'", GreetingBean.class.getSimpleName());
        return new GreetingBean(mafProjectProperty);
    }

    @Bean
    public QuartzJobConfigurationController quartzJobConfigurationController(
            QuartzJobConfigurationService quartzJobConfigurationService
    ) {
        log.warn("Initial bean: '{}'", QuartzJobConfigurationController.class.getSimpleName());
        return new QuartzJobConfigurationController(quartzJobConfigurationService);
    }

    @Bean
    public QuartzJobConfigurationService quartzJobConfigurationService(SchedulerFactoryBean schedulerFactoryBean,
                                                                       MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: '{}'", QuartzJobConfigurationServiceImpl.class.getSimpleName());
        return new QuartzJobConfigurationServiceImpl(schedulerFactoryBean, mafProjectProperty);
    }
}
