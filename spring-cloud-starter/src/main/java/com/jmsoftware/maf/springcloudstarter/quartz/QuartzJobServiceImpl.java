package com.jmsoftware.maf.springcloudstarter.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Map;

/**
 * Description: QuartzJobServiceImpl, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:18 PM
 * @see <a href='https://mp.weixin.qq.com/s/zJSPzcsFl72JNoqZpkH94A'>SpringBoot 整合 Quartz 实现分布式调度</a>
 **/
@Slf4j
@RequiredArgsConstructor
public class QuartzJobServiceImpl implements QuartzJobService {
    private final SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public void addJob(String clazzName, String jobName, String groupName, String cronExp, Map<String, Object> param) {
        try {
            // 构建job信息
            @SuppressWarnings("unchecked") Class<? extends Job> jobClass =
                    (Class<? extends Job>) Class.forName(clazzName);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, groupName).build();
            // 表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(
                    scheduleBuilder).build();
            // 获得JobDataMap，写入数据
            if (param != null) {
                trigger.getJobDataMap().putAll(param);
            }
            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("创建任务失败", e);
        }
    }

    @Override
    public void pauseJob(String jobName, String groupName) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(JobKey.jobKey(jobName, groupName));
        } catch (SchedulerException e) {
            log.error("暂停任务失败", e);
        }
    }

    @Override
    public void resumeJob(String jobName, String groupName) {
        try {
            schedulerFactoryBean.getScheduler().resumeJob(JobKey.jobKey(jobName, groupName));
        } catch (SchedulerException e) {
            log.error("恢复任务失败", e);
        }
    }

    @Override
    public void runOnce(String jobName, String groupName) {
        try {
            schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobName, groupName));
        } catch (SchedulerException e) {
            log.error("立即运行一次定时任务失败", e);
        }
    }

    @Override
    public void updateJob(String jobName, String groupName, String cronExp, Map<String, Object> param) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
            CronTrigger trigger = (CronTrigger) schedulerFactoryBean.getScheduler().getTrigger(triggerKey);
            if (cronExp != null) {
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            }
            //修改map
            if (param != null) {
                trigger.getJobDataMap().putAll(param);
            }
            // 按新的trigger重新设置job执行
            schedulerFactoryBean.getScheduler().rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            log.error("更新任务失败", e);
        }
    }

    @Override
    public void deleteJob(String jobName, String groupName) {
        try {
            //暂停、移除、删除
            schedulerFactoryBean.getScheduler().pauseTrigger(TriggerKey.triggerKey(jobName, groupName));
            schedulerFactoryBean.getScheduler().unscheduleJob(TriggerKey.triggerKey(jobName, groupName));
            schedulerFactoryBean.getScheduler().deleteJob(JobKey.jobKey(jobName, groupName));
        } catch (Exception e) {
            log.error("删除任务失败", e);
        }
    }

    @Override
    public void startAllJobs() {
        try {
            schedulerFactoryBean.start();
        } catch (Exception e) {
            log.error("开启所有的任务失败", e);
        }
    }

    @Override
    public void pauseAllJobs() {
        try {
            schedulerFactoryBean.getScheduler().pauseAll();
        } catch (Exception e) {
            log.error("暂停所有任务失败", e);
        }
    }

    @Override
    public void resumeAllJobs() {
        try {
            schedulerFactoryBean.getScheduler().resumeAll();
        } catch (Exception e) {
            log.error("恢复所有任务失败", e);
        }
    }

    @Override
    public void shutdownAllJobs() {
        try {
            if (!schedulerFactoryBean.getScheduler().isShutdown()) {
                // 需谨慎操作关闭scheduler容器
                // scheduler生命周期结束，无法再 start() 启动 scheduler
                schedulerFactoryBean.getScheduler().shutdown(true);
            }
        } catch (Exception e) {
            log.error("关闭所有的任务失败", e);
        }
    }
}
