package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
import org.springframework.boot.task.TaskSchedulerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

/**
 * # SchedulingConfiguration
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:58 AM
 * @see TaskSchedulingAutoConfiguration
 */
@EnableScheduling
class SchedulingConfiguration(
    private val mafProjectProperties: MafProjectProperties
) {
    companion object {
        const val TASK_SCHEDULER_POOL_SIZE = 2
        private val log = logger()
    }

    @Bean
    fun taskSchedulerCustomizer(): TaskSchedulerCustomizer {
        return TaskSchedulerCustomizer { taskScheduler: ThreadPoolTaskScheduler ->
            taskScheduler.poolSize = TASK_SCHEDULER_POOL_SIZE
            taskScheduler.setThreadNamePrefix("${mafProjectProperties.projectArtifactId}-task-scheduler-")
            taskScheduler.setAwaitTerminationSeconds(10)
            log.warn("Enhanced taskScheduler. poolSize = {}", taskScheduler.poolSize)
        }
    }
}
