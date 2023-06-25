package com.jmsoftware.maf.springcloudstarter.quartz.job

import cn.hutool.core.bean.BeanUtil
import com.jmsoftware.maf.common.util.Slf4j
import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QUARTZ_JOB_CONFIGURATION
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean

/**
 * AbstractQuartzJob
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 3:51 PM
 */
@Slf4j
abstract class AbstractQuartzJob : QuartzJobBean() {
    companion object {
        private val QUARTZ_JOB_CONFIGURATION_CLASS = QuartzJobConfiguration::class.java.name
    }

    override fun executeInternal(context: JobExecutionContext) {
        val sourceQuartzJobConfiguration = context.mergedJobDataMap[QUARTZ_JOB_CONFIGURATION]
        require(QUARTZ_JOB_CONFIGURATION_CLASS == sourceQuartzJobConfiguration!!.javaClass.name) {
            "Invalid job data! Not the instance of QuartzJobConfiguration. Runtime actual class: ${sourceQuartzJobConfiguration.javaClass}"
        }
        log.atDebug().log { "Found and QuartzJobConfiguration from job data map: $sourceQuartzJobConfiguration" }
        val quartzJobConfiguration = QuartzJobConfiguration()
        BeanUtil.copyProperties(sourceQuartzJobConfiguration, quartzJobConfiguration)
        try {
            this.invoke(context, quartzJobConfiguration)
        } catch (e: Exception) {
            log.error("Exception occurred when invoking method!", e)
        }
    }

    /**
     * Invoke.
     *
     * @param context                the context
     * @param quartzJobConfiguration the quartz job configuration
     */
    protected abstract operator fun invoke(
        context: JobExecutionContext,
        quartzJobConfiguration: QuartzJobConfiguration
    )
}
