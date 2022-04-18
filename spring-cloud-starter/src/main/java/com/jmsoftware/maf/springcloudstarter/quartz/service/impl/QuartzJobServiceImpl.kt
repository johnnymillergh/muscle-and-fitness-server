package com.jmsoftware.maf.springcloudstarter.quartz.service.impl

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobService
import org.quartz.*
import org.springframework.lang.Nullable
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import java.util.function.UnaryOperator

/**
 * # QuartzJobServiceImpl
 *
 * Description: QuartzJobServiceImpl, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 9:05 AM
 * @see <a href='https://mp.weixin.qq.com/s/zJSPzcsFl72JNoqZpkH94A'>SpringBoot 整合 Quartz 实现分布式调度</a>
 */
@Service
class QuartzJobServiceImpl(
    private val schedulerFactoryBean: SchedulerFactoryBean
) : QuartzJobService {
    companion object {
        const val JOB_DETAIL_POSTFIX = "-job-detail"
        const val TRIGGER_POSTFIX = "-trigger"
        private val JOB_DETAIL_NAME_OPERATOR = UnaryOperator { jobName: String -> jobName + JOB_DETAIL_POSTFIX }
        private val TRIGGER_NAME_OPERATOR = UnaryOperator { jobName: String -> jobName + TRIGGER_POSTFIX }
        private val log = logger()
    }

    override fun addJob(
        clazzName: String,
        jobName: String,
        groupName: String,
        cronExp: String,
        param: Map<String, Any>?
    ) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName)
        // Build job
        val jobClass = Class.forName(clazzName).asSubclass(
            Job::class.java
        )
        val jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobDetailName, groupName).build()
        // Build cron-expression schedule
        val scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp)
        val triggerName = TRIGGER_NAME_OPERATOR.apply(jobName)
        // Build trigger
        val trigger = TriggerBuilder
            .newTrigger()
            .withIdentity(triggerName, groupName)
            .withSchedule(scheduleBuilder)
            .build()
        trigger.jobDataMap.putAll(param ?: emptyMap())
        schedulerFactoryBean.scheduler.scheduleJob(jobDetail, trigger)
        log.info("Added job to scheduler. jobDetailName: $jobDetailName, triggerName: $triggerName")
    }

    override fun pauseJob(jobName: String, groupName: String) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName)
        schedulerFactoryBean.scheduler.pauseJob(JobKey.jobKey(jobDetailName, groupName))
        log.info("Paused job. jobDetailName: {}", jobDetailName)
    }

    override fun resumeJob(jobName: String, groupName: String) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName)
        schedulerFactoryBean.scheduler.resumeJob(JobKey.jobKey(jobDetailName, groupName))
        log.info("Resumed job. jobDetailName: {}", jobDetailName)
    }

    override fun runImmediately(jobName: String, groupName: String) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName)
        schedulerFactoryBean.scheduler.triggerJob(JobKey.jobKey(jobDetailName, groupName))
        log.info(
            "Triggered the identified JobDetail (execute it now). jobDetailName: {}",
            jobDetailName
        )
    }

    override fun updateJob(
        jobName: String, groupName: String, cronExp: String,
        @Nullable param: Map<String, Any>?
    ) {
        val triggerName = TRIGGER_NAME_OPERATOR.apply(jobName)
        val triggerKey = TriggerKey.triggerKey(triggerName, groupName)
        var trigger = schedulerFactoryBean.scheduler.getTrigger(triggerKey) as CronTrigger
        if (StrUtil.isNotBlank(cronExp)) {
            val scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp)
            trigger = trigger.triggerBuilder.withIdentity(triggerKey).withSchedule(scheduleBuilder).build()
        }
        trigger.jobDataMap.putAll(param ?: emptyMap())
        schedulerFactoryBean.scheduler.rescheduleJob(triggerKey, trigger)
        log.info("Updated job. triggerName: {}", triggerName)
    }

    override fun deleteJob(jobName: String, groupName: String) {
        val triggerName = TRIGGER_NAME_OPERATOR.apply(jobName)
        // Pause, Remove the trigger and the job, and then delete the job.
        schedulerFactoryBean.scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, groupName))
        schedulerFactoryBean.scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, groupName))
        schedulerFactoryBean.scheduler.deleteJob(JobKey.jobKey(jobName, groupName))
        log.info("Updated job. triggerName: {}", triggerName)
    }

    override fun startAllJobs() {
        schedulerFactoryBean.start()
        log.info("Started jobs.")
    }

    override fun pauseAllJobs() {
        schedulerFactoryBean.scheduler.pauseAll()
        log.info("Paused all jobs.")
    }

    override fun resumeAllJobs() {
        schedulerFactoryBean.scheduler.resumeAll()
        log.info("Resumed all jobs.")
    }

    override fun shutdownAllJobs() {
        throw IllegalCallerException("Not supported operation!")
    }
}
