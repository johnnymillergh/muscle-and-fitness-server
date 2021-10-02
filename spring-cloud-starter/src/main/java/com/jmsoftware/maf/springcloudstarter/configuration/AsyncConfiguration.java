package com.jmsoftware.maf.springcloudstarter.configuration;

import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

/**
 * Description: AsyncConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/29/2021 4:39 PM
 * @see TaskExecutionAutoConfiguration
 **/
@Slf4j
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfiguration {
    private static final int QUEUE_CAPACITY = 10000;
    private final MafProjectProperties mafProjectProperties;

    /**
     * <p>Note: In the above example the {@code ThreadPoolTaskExecutor} is not a fully managed
     * Spring bean. Add the {@code @Bean} annotation to the {@code getAsyncExecutor()} method
     * if you want a fully managed bean. In such circumstances it is no longer necessary to
     * manually call the {@code executor.initialize()} method as this will be invoked
     * automatically when the bean is initialized.
     * </p>
     *
     * @return customized async task executor
     * @see ThreadPoolTaskExecutor
     */
    @Bean
    public TaskExecutorCustomizer asyncTaskExecutorCustomizer() {
        val corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        return taskExecutor -> {
            taskExecutor.setCorePoolSize(corePoolSize);
            taskExecutor.setMaxPoolSize(corePoolSize * 3);
            taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
            taskExecutor.setBeanName("asyncTaskExecutor");
            taskExecutor.setThreadNamePrefix(format("{}-async-", this.mafProjectProperties.getProjectArtifactId()));
            // Specify the RejectedExecutionHandler to use for the ExecutorService.
            // Default is the ExecutorService's default abort policy.
            taskExecutor.setRejectedExecutionHandler(
                    (runnable, executor1) ->
                            log.error(
                                    "Captured rejected execution. Runnable: {}, executor task count: {}",
                                    runnable.toString(), executor1.getTaskCount()
                            )
            );
            log.warn("Enhanced taskExecutor. corePoolSize = {}, maxPoolSize = {}, queueCapacity = {}",
                     taskExecutor.getCorePoolSize(), taskExecutor.getMaxPoolSize(), QUEUE_CAPACITY);
        };
    }

    @Bean
    public AsyncConfigurer asyncConfigurer(
            @Qualifier(APPLICATION_TASK_EXECUTOR_BEAN_NAME) ThreadPoolTaskExecutor threadPoolTaskExecutor
    ) {
        log.warn("Initial bean: '{}'", AsyncConfigurer.class.getSimpleName());
        return new AsyncConfigurer() {
            @Override
            public Executor getAsyncExecutor() {
                return threadPoolTaskExecutor;
            }

            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return new SimpleAsyncUncaughtExceptionHandler();
            }
        };
    }

    @Bean
    public CallableProcessingInterceptor callableProcessingInterceptor() {
        log.warn("Initial bean: {}", CallableProcessingInterceptor.class.getSimpleName());
        return new TimeoutCallableProcessingInterceptor() {
            @Override
            public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
                log.error("Handling task timeout!");
                return super.handleTimeout(request, task);
            }
        };
    }
}
