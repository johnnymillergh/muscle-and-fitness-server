package com.jmsoftware.maf.springcloudstarter.quartz.util

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.quartz.constant.Concurrent
import com.jmsoftware.maf.springcloudstarter.quartz.constant.MisfirePolicy
import com.jmsoftware.maf.springcloudstarter.quartz.constant.MisfirePolicy.*
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QUARTZ_JOB_CONFIGURATION
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QuartzJobStatus
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import com.jmsoftware.maf.springcloudstarter.quartz.job.QuartzDisallowConcurrentExecution
import com.jmsoftware.maf.springcloudstarter.quartz.job.QuartzJobExecution
import com.jmsoftware.maf.springcloudstarter.quartz.util.ScheduleUtil.JOB_DETAIL_NAME_OPERATOR
import com.jmsoftware.maf.springcloudstarter.quartz.util.ScheduleUtil.TRIGGER_NAME_OPERATOR
import com.jmsoftware.maf.springcloudstarter.quartz.util.ScheduleUtil.log
import org.quartz.*
import java.util.*
import java.util.function.BiFunction

/**
 * # ScheduleUtil
 *
 * The type Schedule util.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 5:14 PM
 */
private object ScheduleUtil {
    val log = logger()
    private const val JOB_DETAIL_POSTFIX = "-job-detail"
    private const val TRIGGER_POSTFIX = "-trigger"
    val JOB_DETAIL_NAME_OPERATOR = BiFunction { jobId: Long, serviceName: String ->
        "$serviceName#$jobId$JOB_DETAIL_POSTFIX"
    }
    val TRIGGER_NAME_OPERATOR = BiFunction { jobId: Long, serviceName: String ->
        "$serviceName#$jobId$TRIGGER_POSTFIX"
    }
}

/**
 * Gets quartz job class.
 *
 * @param quartzJobConfiguration the sys job
 * @return the quartz job class
 */
private fun getQuartzJobClass(
    quartzJobConfiguration: QuartzJobConfiguration
): Class<out Job> {
    val concurrent = Concurrent.CONCURRENT.value == quartzJobConfiguration.concurrent
    return if (concurrent) QuartzJobExecution::class.java else QuartzDisallowConcurrentExecution::class.java
}

/**
 * Gets job key.
 *
 * @param jobId    the job id
 * @param jobGroup the job group
 * @return the job key
 */
fun getJobKey(
    jobId: Long,
    jobGroup: String,
    serviceName: String
): JobKey {
    return JobKey.jobKey(JOB_DETAIL_NAME_OPERATOR.apply(jobId, serviceName), jobGroup)
}

/**
 * Gets trigger key.
 *
 * @param jobId    the job id
 * @param jobGroup the job group
 * @return the trigger key
 */
private fun getTriggerKey(
    jobId: Long,
    jobGroup: String,
    serviceName: String
): TriggerKey {
    return TriggerKey.triggerKey(TRIGGER_NAME_OPERATOR.apply(jobId, serviceName), jobGroup)
}

/**
 * Create schedule job.
 *
 * @param scheduler              the scheduler
 * @param quartzJobConfiguration the quartz job configuration
 * @throws SchedulerException the scheduler exception
 */
fun createScheduleJob(
    scheduler: Scheduler,
    quartzJobConfiguration: QuartzJobConfiguration,
    serviceName: String
): JobDetail? {
    if (!StrUtil.equals(quartzJobConfiguration.serviceName, serviceName)) {
        log.warn("The service name is not equal to quartzJobConfiguration's serviceName. serviceName: $serviceName, $quartzJobConfiguration")
        return null
    }
    val jobClass = getQuartzJobClass(quartzJobConfiguration)
    val jobId = quartzJobConfiguration.id
    val jobGroup = quartzJobConfiguration.group
    val jobDetail = JobBuilder
        .newJob(jobClass)
        .withIdentity(getJobKey(jobId!!, jobGroup!!, serviceName))
        .withDescription(quartzJobConfiguration.description)
        .build()
    jobDetail.jobDataMap[QUARTZ_JOB_CONFIGURATION] = quartzJobConfiguration
    val cronScheduleBuilder = handleCronScheduleMisfirePolicy(
        quartzJobConfiguration,
        CronScheduleBuilder.cronSchedule(quartzJobConfiguration.cronExpression)
    )
    val trigger = TriggerBuilder
        .newTrigger()
        .withIdentity(getTriggerKey(jobId, jobGroup, serviceName))
        .withSchedule(cronScheduleBuilder)
        .build()
    if (scheduler.checkExists(getJobKey(jobId, jobGroup, serviceName))) {
        scheduler.deleteJob(getJobKey(jobId, jobGroup, serviceName))
    }
    scheduler.scheduleJob(jobDetail, trigger)
    if (QuartzJobStatus.PAUSE.value == quartzJobConfiguration.status) {
        scheduler.pauseJob(getJobKey(jobId, jobGroup, serviceName))
    }
    return jobDetail
}

/**
 * Handle cron schedule misfire policy cron schedule builder.
 *
 * @param quartzJobConfiguration the quartz job configuration
 * @param cronScheduleBuilder    the cron schedule builder
 * @return the cron schedule builder
 */
private fun handleCronScheduleMisfirePolicy(
    quartzJobConfiguration: QuartzJobConfiguration,
    cronScheduleBuilder: CronScheduleBuilder
): CronScheduleBuilder {
    val misfirePolicy: MisfirePolicy =
        Optional.ofNullable(MisfirePolicy.getByValue(quartzJobConfiguration.misfirePolicy!!))
            .orElseThrow { IllegalArgumentException("MisfirePolicy not exists! Value: ${quartzJobConfiguration.misfirePolicy}") }
    return when (misfirePolicy) {
        MISFIRE_INSTRUCTION_SMART_POLICY -> cronScheduleBuilder
        MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY -> cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires()
        MISFIRE_INSTRUCTION_FIRE_ONCE_NOW -> cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed()
        MISFIRE_INSTRUCTION_DO_NOTHING -> cronScheduleBuilder.withMisfireHandlingInstructionDoNothing()
    }
}
