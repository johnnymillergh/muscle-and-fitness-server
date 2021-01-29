package com.jmsoftware.maf.springbootstarter.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Description: AsyncConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/29/2021 4:39 PM
 **/
@Slf4j
@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration implements AsyncConfigurer {
    @Bean
    @Override
    public AsyncTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setMaxPoolSize(100);
        executor.setBeanName("spring-boot-starter-thread-pool-task-executor");
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
