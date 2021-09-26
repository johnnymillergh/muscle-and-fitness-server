package com.jmsoftware.maf.springcloudstarter.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.springcloudstarter.quartz.constant.Concurrent;
import com.jmsoftware.maf.springcloudstarter.quartz.constant.MisfirePolicy;
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QuartzJobStatus;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import com.jmsoftware.maf.springcloudstarter.quartz.job.QuartzDisallowConcurrentExecution;
import com.jmsoftware.maf.springcloudstarter.quartz.job.QuartzJobExecution;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.*;

import java.util.Optional;
import java.util.function.BiFunction;

import static com.jmsoftware.maf.springcloudstarter.quartz.constant.ScheduleConstant.QUARTZ_JOB_CONFIGURATION;

/**
 * The type Schedule util.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 10:20 AM
 */
@Slf4j
public class ScheduleUtil {
    public static final String JOB_DETAIL_POSTFIX = "-job-detail";
    public static final String TRIGGER_POSTFIX = "-trigger";
    public static final BiFunction<Long, String, String> JOB_DETAIL_NAME_OPERATOR =
            (jobId, serviceName) -> String.format("%s#%d%s", serviceName, jobId, JOB_DETAIL_POSTFIX);
    public static final BiFunction<Long, String, String> TRIGGER_NAME_OPERATOR =
            (jobId, serviceName) -> String.format("%s#%d%s", serviceName, jobId, TRIGGER_POSTFIX);

    private ScheduleUtil() {
    }

    /**
     * Gets quartz job class.
     *
     * @param quartzJobConfiguration the sys job
     * @return the quartz job class
     */
    private static Class<? extends Job> getQuartzJobClass(QuartzJobConfiguration quartzJobConfiguration) {
        val concurrent = Concurrent.CONCURRENT.getValue().equals(quartzJobConfiguration.getConcurrent());
        return concurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * Gets job key.
     *
     * @param jobId    the job id
     * @param jobGroup the job group
     * @return the job key
     */
    public static JobKey getJobKey(Long jobId, String jobGroup, String serviceName) {
        return JobKey.jobKey(JOB_DETAIL_NAME_OPERATOR.apply(jobId, serviceName), jobGroup);
    }

    /**
     * Gets trigger key.
     *
     * @param jobId    the job id
     * @param jobGroup the job group
     * @return the trigger key
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup, String serviceName) {
        return TriggerKey.triggerKey(TRIGGER_NAME_OPERATOR.apply(jobId, serviceName), jobGroup);
    }

    /**
     * Create schedule job.
     *
     * @param scheduler              the scheduler
     * @param quartzJobConfiguration the quartz job configuration
     * @throws SchedulerException the scheduler exception
     */
    public static JobDetail createScheduleJob(Scheduler scheduler,
                                              QuartzJobConfiguration quartzJobConfiguration,
                                              String serviceName) throws SchedulerException {
        if (!StrUtil.equals(quartzJobConfiguration.getServiceName(), serviceName)) {
            log.warn("The service name is not equal to quartzJobConfiguration's serviceName. serviceName: {}, {}",
                     serviceName, quartzJobConfiguration);
            return null;
        }
        val jobClass = getQuartzJobClass(quartzJobConfiguration);
        val jobId = quartzJobConfiguration.getId();
        val jobGroup = quartzJobConfiguration.getGroup();
        val jobDetail = JobBuilder
                .newJob(jobClass)
                .withIdentity(getJobKey(jobId, jobGroup, serviceName))
                .build();
        jobDetail.getJobDataMap().put(QUARTZ_JOB_CONFIGURATION, quartzJobConfiguration);
        val cronScheduleBuilder = handleCronScheduleMisfirePolicy(
                quartzJobConfiguration,
                CronScheduleBuilder.cronSchedule(quartzJobConfiguration.getCronExpression())
        );
        val trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(getTriggerKey(jobId, jobGroup, serviceName))
                .withSchedule(cronScheduleBuilder)
                .build();
        if (scheduler.checkExists(getJobKey(jobId, jobGroup, serviceName))) {
            scheduler.deleteJob(getJobKey(jobId, jobGroup, serviceName));
        }
        scheduler.scheduleJob(jobDetail, trigger);
        if (QuartzJobStatus.PAUSE.getValue().equals(quartzJobConfiguration.getStatus())) {
            scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup, serviceName));
        }
        return jobDetail;
    }

    /**
     * Handle cron schedule misfire policy cron schedule builder.
     *
     * @param job the job
     * @param cb  the cb
     * @return the cron schedule builder
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(QuartzJobConfiguration job,
                                                                      CronScheduleBuilder cb) {
        val misfirePolicy = Optional
                .ofNullable(MisfirePolicy.getByValue(job.getMisfirePolicy()))
                .orElseThrow();
        switch (misfirePolicy) {
            case MISFIRE_INSTRUCTION_SMART_POLICY:
                return cb;
            case MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case MISFIRE_INSTRUCTION_FIRE_ONCE_NOW:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case MISFIRE_INSTRUCTION_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new IllegalArgumentException(
                        String.format("The task misfire policy '%s' cannot be used in cron schedule tasks",
                                      job.getMisfirePolicy()));
        }
    }
}
