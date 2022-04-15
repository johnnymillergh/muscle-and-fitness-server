package com.jmsoftware.maf.springcloudstarter.quartz.job

import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import com.jmsoftware.maf.springcloudstarter.quartz.util.QuartzJobInvocationUtil
import org.quartz.JobExecutionContext

/**
 * # QuartzJobExecution
 *
 * QuartzJobExecution. Concurrent execution
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:45 PM
 */
class QuartzJobExecution : AbstractQuartzJob() {
    override fun invoke(context: JobExecutionContext, quartzJobConfiguration: QuartzJobConfiguration) {
        QuartzJobInvocationUtil.invokeMethod(quartzJobConfiguration)
    }
}
