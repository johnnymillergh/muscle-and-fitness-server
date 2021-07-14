package com.jmsoftware.maf.springcloudstarter.quartz;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Description: QuartzJobServiceImpl, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:18 PM
 * @see <a href='https://mp.weixin.qq.com/s/zJSPzcsFl72JNoqZpkH94A'>SpringBoot 整合 Quartz 实现分布式调度</a>
 **/
@Slf4j
@RequiredArgsConstructor
public class QuartzJobServiceImpl implements QuartzJobService {
    public static final String JOB_DETAIL_POSTFIX = "-job-detail";
    public static final String TRIGGER_POSTFIX = "-trigger";
    private static final UnaryOperator<String> JOB_DETAIL_NAME_OPERATOR = jobName -> jobName + JOB_DETAIL_POSTFIX;
    private static final UnaryOperator<String> TRIGGER_NAME_OPERATOR = jobName -> jobName + JOB_DETAIL_POSTFIX;
    private final SchedulerFactoryBean schedulerFactoryBean;

    @Override
    @SneakyThrows
    public void addJob(String clazzName, String jobName, String groupName, String cronExp, Map<String, Object> param) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName);
        // Build job
        val jobClass = Class.forName(clazzName).asSubclass(Job.class);
        val jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobDetailName, groupName).build();
        // Build cron-expression schedule
        val scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
        val triggerName = jobName + TRIGGER_POSTFIX;
        // Build trigger
        val trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(triggerName, groupName)
                .withSchedule(scheduleBuilder)
                .build();
        if (ObjectUtil.isNotNull(param)) {
            trigger.getJobDataMap().putAll(param);
        }
        this.schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
        log.info("Added job to scheduler. jobDetailName: {}, triggerName: {}", jobDetailName, triggerName);
    }

    @Override
    @SneakyThrows
    public void pauseJob(String jobName, String groupName) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName);
        this.schedulerFactoryBean.getScheduler().pauseJob(JobKey.jobKey(jobDetailName, groupName));
        log.info("Paused job. jobDetailName: {}", jobDetailName);
    }

    @Override
    @SneakyThrows
    public void resumeJob(String jobName, String groupName) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName);
        this.schedulerFactoryBean.getScheduler().resumeJob(JobKey.jobKey(jobDetailName, groupName));
        log.info("Resumed job. jobDetailName: {}", jobDetailName);
    }

    @Override
    @SneakyThrows
    public void runImmediately(String jobName, String groupName) {
        val jobDetailName = JOB_DETAIL_NAME_OPERATOR.apply(jobName);
        this.schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobDetailName, groupName));
        log.info("Triggered the identified JobDetail (execute it now). jobDetailName: {}", jobDetailName);
    }

    @Override
    @SneakyThrows
    public void updateJob(String jobName, String groupName, String cronExp, Map<String, Object> param) {
        val triggerName = TRIGGER_NAME_OPERATOR.apply(jobName);
        val triggerKey = TriggerKey.triggerKey(triggerName, groupName);
        var trigger = (CronTrigger) this.schedulerFactoryBean.getScheduler().getTrigger(triggerKey);
        if (StrUtil.isNotBlank(cronExp)) {
            val scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        }
        if (ObjectUtil.isNotNull(param)) {
            trigger.getJobDataMap().putAll(param);
        }
        this.schedulerFactoryBean.getScheduler().rescheduleJob(triggerKey, trigger);
        log.info("Updated job. triggerName: {}", triggerName);
    }

    @Override
    @SneakyThrows
    public void deleteJob(String jobName, String groupName) {
        val triggerName = TRIGGER_NAME_OPERATOR.apply(jobName);
        // Pause, Remove the trigger and the job, and then delete the job.
        this.schedulerFactoryBean.getScheduler().pauseTrigger(TriggerKey.triggerKey(jobName, groupName));
        this.schedulerFactoryBean.getScheduler().unscheduleJob(TriggerKey.triggerKey(jobName, groupName));
        this.schedulerFactoryBean.getScheduler().deleteJob(JobKey.jobKey(jobName, groupName));
        log.info("Updated job. triggerName: {}", triggerName);
    }

    @Override
    @SneakyThrows
    public void startAllJobs() {
        this.schedulerFactoryBean.start();
        log.info("Started jobs.");
    }

    @Override
    @SneakyThrows
    public void pauseAllJobs() {
        this.schedulerFactoryBean.getScheduler().pauseAll();
        log.info("Paused all jobs.");
    }

    @Override
    @SneakyThrows
    public void resumeAllJobs() {
        this.schedulerFactoryBean.getScheduler().resumeAll();
        log.info("Resumed all jobs.");
    }

    @Override
    public void shutdownAllJobs() {
        throw new IllegalCallerException("Not supported operation!");
    }
}
