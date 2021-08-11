package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Description: AsyncConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/29/2021 4:39 PM
 **/
@Slf4j
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfiguration {
    private final MafProjectProperty mafProjectProperty;

    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        val executor = new ThreadPoolTaskExecutor();
        val corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        log.info("corePoolSize = {}, for AsyncTaskExecutor", corePoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize * 3);
        executor.setBeanName("async-task-executor");
        executor.setThreadNamePrefix(String.format("%s-", this.mafProjectProperty.getProjectArtifactId()));
        // Specify the RejectedExecutionHandler to use for the ExecutorService.
        // Default is the ExecutorService's default abort policy.
        executor.setRejectedExecutionHandler((runnable, executor1) -> {
            log.error("Captured rejected execution. {}", runnable);
            // TODO: runnable persistence
        });
        executor.initialize();
        log.warn("Initial bean: '{}'", AsyncTaskExecutor.class.getSimpleName());
        return executor;
    }
}
