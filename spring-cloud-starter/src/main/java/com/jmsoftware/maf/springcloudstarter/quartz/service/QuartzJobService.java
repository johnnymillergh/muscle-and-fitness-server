package com.jmsoftware.maf.springcloudstarter.quartz.service;

import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * Description: QuartzJobService, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:17 PM
 */
@Validated
public interface QuartzJobService {
    /**
     * Add job.
     *
     * @param clazzName the clazz name
     * @param jobName   the job name
     * @param groupName the group name
     * @param cronExp   the cron exp
     * @param param     the param
     */
    void addJob(
            @NotBlank String clazzName,
            @NotBlank String jobName,
            @NotBlank String groupName,
            @NotBlank String cronExp,
            @Nullable Map<String, Object> param
    );

    /**
     * Pause job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void pauseJob(@NotBlank String jobName, @NotBlank String groupName);

    /**
     * Resume job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void resumeJob(@NotBlank String jobName, @NotBlank String groupName);

    /**
     * Run immediately.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void runImmediately(@NotBlank String jobName, @NotBlank String groupName);

    /**
     * Update job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     * @param cronExp   the cron exp
     * @param param     the param
     */
    void updateJob(
            @NotBlank String jobName,
            @NotBlank String groupName,
            @NotBlank String cronExp,
            @Nullable Map<String, Object> param
    );

    /**
     * Delete job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void deleteJob(@NotBlank String jobName, @NotBlank String groupName);

    /**
     * Start all jobs.
     */
    void startAllJobs();

    /**
     * Pause all jobs.
     */
    void pauseAllJobs();

    /**
     * Resume all jobs.
     */
    void resumeAllJobs();

    /**
     * Shutdown all jobs.
     */
    void shutdownAllJobs();
}
