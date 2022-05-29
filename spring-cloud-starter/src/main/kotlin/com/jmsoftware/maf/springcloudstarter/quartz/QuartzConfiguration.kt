package com.jmsoftware.maf.springcloudstarter.quartz

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.quartz.controller.QuartzJobConfigurationController
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService
import com.jmsoftware.maf.springcloudstarter.quartz.service.impl.QuartzJobConfigurationServiceImpl
import org.quartz.Scheduler
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration
import org.springframework.boot.autoconfigure.quartz.QuartzProperties
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.validation.Validator

/**
 * # QuartzConfiguration
 *
 * Description: QuartzConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 7:19 PM
 * @see QuartzAutoConfiguration
 */
@ConditionalOnClass(Scheduler::class, SchedulerFactoryBean::class, PlatformTransactionManager::class)
class QuartzConfiguration(
    private val mafProjectProperties: MafProjectProperties
) {
    companion object {
        private val log = logger()
    }

    @Bean
    fun schedulerFactoryBeanCustomizer(quartzProperties: QuartzProperties): SchedulerFactoryBeanCustomizer {
        val cpuCoreCount = Runtime.getRuntime().availableProcessors()
        val threadCount = (2 * cpuCoreCount).toString()
        quartzProperties.properties[SchedulerFactoryBean.PROP_THREAD_COUNT] = threadCount
        return SchedulerFactoryBeanCustomizer { schedulerFactoryBean: SchedulerFactoryBean ->
            schedulerFactoryBean.setQuartzProperties(asProperties(quartzProperties.properties))
            log.warn("Quartz thread pool enhanced by current cpuCoreCount: $cpuCoreCount, threadCount: $threadCount")
            schedulerFactoryBean.setSchedulerName("${mafProjectProperties.projectArtifactId}-quartz-scheduler")
            schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true)
            schedulerFactoryBean.setBeanName("schedulerFactoryBean")
        }
    }

    private fun asProperties(source: Map<String, String>): Properties {
        val properties = Properties()
        properties.putAll(source)
        return properties
    }

    @Bean
    fun greetingBean(): GreetingBean {
        log.warn("Initial bean: `${GreetingBean::class.java.simpleName}`")
        return GreetingBean(mafProjectProperties)
    }

    @Bean
    fun quartzJobConfigurationController(
        quartzJobConfigurationService: QuartzJobConfigurationService
    ): QuartzJobConfigurationController {
        log.warn("Initial bean: `${QuartzJobConfigurationController::class.java.simpleName}`")
        return QuartzJobConfigurationController(quartzJobConfigurationService)
    }

    @Bean
    fun quartzJobConfigurationService(schedulerFactoryBean: SchedulerFactoryBean, validator: Validator): QuartzJobConfigurationService {
        log.warn("Initial bean: `${QuartzJobConfigurationServiceImpl::class.java.simpleName}`")
        return QuartzJobConfigurationServiceImpl(schedulerFactoryBean, mafProjectProperties, validator)
    }
}
