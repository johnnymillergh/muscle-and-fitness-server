package com.jmsoftware.maf.springcloudstarter.quartz;

import java.util.Map;

/**
 * Description: QuartzJobService, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:17 PM
 */
public interface QuartzJobService {
    /**
     * 添加任务可以传参数
     *
     * @param clazzName the clazz name
     * @param jobName   the job name
     * @param groupName the group name
     * @param cronExp   the cron exp
     * @param param     the param
     */
    void addJob(String clazzName, String jobName, String groupName, String cronExp, Map<String, Object> param);

    /**
     * 暂停任务
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void pauseJob(String jobName, String groupName);

    /**
     * 恢复任务
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void resumeJob(String jobName, String groupName);

    /**
     * 立即运行一次定时任务
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void runImmediately(String jobName, String groupName);

    /**
     * 更新任务
     *
     * @param jobName   the job name
     * @param groupName the group name
     * @param cronExp   the cron exp
     * @param param     the param
     */
    void updateJob(String jobName, String groupName, String cronExp, Map<String, Object> param);

    /**
     * 删除任务
     *
     * @param jobName   the job name
     * @param groupName the group name
     */
    void deleteJob(String jobName, String groupName);

    /**
     * 启动所有任务
     */
    void startAllJobs();

    /**
     * 暂停所有任务
     */
    void pauseAllJobs();

    /**
     * 恢复所有任务
     */
    void resumeAllJobs();

    /**
     * 关闭所有任务
     */
    void shutdownAllJobs();
}
