package com.jmsoftware.maf.springcloudstarter.quartz.service

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

/**
 * # QuartzJobService
 *
 * Description: QuartzJobService, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:50 PM
 */
@Validated
interface QuartzJobService {
    /**
     * Add job.
     *
     * @param clazzName the clazz name
     * @param jobName   the job name
     * @param groupName the group name
     * @param cronExp   the cron exp
     * @param param     the param
     */
    fun addJob(
        clazzName: @NotBlank String,
        jobName: @NotBlank String,
        groupName: @NotBlank String,
        cronExp: @NotBlank String,
        param: Map<String, Any>?
    )

    /**
     * Pause job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    fun pauseJob(jobName: @NotBlank String, groupName: @NotBlank String)

    /**
     * Resume job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    fun resumeJob(jobName: @NotBlank String, groupName: @NotBlank String)

    /**
     * Run immediately.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    fun runImmediately(jobName: @NotBlank String, groupName: @NotBlank String)

    /**
     * Update job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     * @param cronExp   the cron exp
     * @param param     the param
     */
    fun updateJob(
        jobName: @NotBlank String,
        groupName: @NotBlank String,
        cronExp: @NotBlank String,
        param: Map<String, Any>?
    )

    /**
     * Delete job.
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    fun deleteJob(jobName: @NotBlank String, groupName: @NotBlank String)

    /**
     * Start all jobs.
     */
    fun startAllJobs()

    /**
     * Pause all jobs.
     */
    fun pauseAllJobs()

    /**
     * Resume all jobs.
     */
    fun resumeAllJobs()

    /**
     * Shutdown all jobs.
     */
    fun shutdownAllJobs()
}
