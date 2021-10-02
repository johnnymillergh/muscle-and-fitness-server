package com.jmsoftware.maf.springcloudstarter.configuration;

import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * <h1>SchedulingConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 10/2/21 2:30 PM
 * @see TaskSchedulingAutoConfiguration
 **/
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfiguration {
    public static final int TASK_SCHEDULER_POOL_SIZE = 2;
    private final MafProjectProperties mafProjectProperties;

    @Bean
    public TaskSchedulerCustomizer taskSchedulerCustomizer() {
        return taskScheduler -> {
            taskScheduler.setPoolSize(TASK_SCHEDULER_POOL_SIZE);
            taskScheduler.setThreadNamePrefix(
                    format("{}-task-scheduler-", this.mafProjectProperties.getProjectArtifactId())
            );
            taskScheduler.setAwaitTerminationSeconds(10);
            log.warn("Enhanced taskScheduler. poolSize = {}", taskScheduler.getPoolSize());
        };
    }
}
