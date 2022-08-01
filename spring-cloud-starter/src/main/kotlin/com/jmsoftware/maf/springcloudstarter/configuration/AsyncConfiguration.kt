package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME
import org.springframework.boot.task.TaskExecutorCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.async.CallableProcessingInterceptor
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

/**
 * # AsyncConfiguration
 *
 * Description: AsyncConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:26 AM
 * @see TaskExecutionAutoConfiguration
 */
@EnableAsync
class AsyncConfiguration(
    private val mafProjectProperties: MafProjectProperties
) {
    companion object {
        private const val QUEUE_CAPACITY = 10_000
        private val log = logger()
    }

    /**
     *
     * Note: In the above example the `ThreadPoolTaskExecutor` is not a fully managed
     * Spring bean. Add the `@Bean` annotation to the `getAsyncExecutor()` method
     * if you want a fully managed bean. In such circumstances it is no longer necessary to
     * manually call the `executor.initialize()` method as this will be invoked
     * automatically when the bean is initialized.
     *
     *
     * @return customized async task executor
     * @see ThreadPoolTaskExecutor
     */
    @Bean
    fun asyncTaskExecutorCustomizer(): TaskExecutorCustomizer {
        val corePoolSize = Runtime.getRuntime().availableProcessors() * 2
        return TaskExecutorCustomizer { taskExecutor: ThreadPoolTaskExecutor ->
            taskExecutor.corePoolSize = corePoolSize
            taskExecutor.maxPoolSize = corePoolSize * 3
            taskExecutor.queueCapacity = QUEUE_CAPACITY
            taskExecutor.setBeanName("asyncTaskExecutor")
            taskExecutor.setThreadNamePrefix("${mafProjectProperties.projectArtifactId}-async-executor-")
            // Specify the RejectedExecutionHandler to use for the ExecutorService.
            // Default is the ExecutorService's default abort policy.
            taskExecutor.setRejectedExecutionHandler { runnable: Runnable, executor1: ThreadPoolExecutor ->
                log.error("Captured rejected execution. Runnable: $runnable, executor task count: ${executor1.taskCount}")
            }
            log.warn("Enhanced taskExecutor. corePoolSize = ${taskExecutor.corePoolSize}, maxPoolSize = ${taskExecutor.maxPoolSize}, queueCapacity = $QUEUE_CAPACITY")
        }
    }

    @Bean
    fun asyncConfigurer(
        @Qualifier(APPLICATION_TASK_EXECUTOR_BEAN_NAME) threadPoolTaskExecutor: ThreadPoolTaskExecutor
    ): AsyncConfigurer {
        log.warn("Initial bean: `${AsyncConfigurer::class.java.simpleName}`")
        return object : AsyncConfigurer {
            override fun getAsyncExecutor(): Executor {
                return threadPoolTaskExecutor
            }

            override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
                return SimpleAsyncUncaughtExceptionHandler()
            }
        }
    }

    @Bean
    fun callableProcessingInterceptor(): CallableProcessingInterceptor {
        log.warn("Initial bean: `${CallableProcessingInterceptor::class.java.simpleName}`")
        return object : TimeoutCallableProcessingInterceptor() {
            override fun <T> handleTimeout(request: NativeWebRequest, task: Callable<T>): Any {
                log.error("Handling task timeout!")
                return super.handleTimeout(request, task)
            }
        }
    }
}
