package com.jmsoftware.maf.springcloudstarter.eventbus

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * # EventBugConfiguration
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/23/22 7:52 AM
 **/
@Configuration
@ConditionalOnClass(EventBus::class)
class EventBugConfiguration(
    private val mafProjectProperties: MafProjectProperties,
    private val applicationContext: ApplicationContext,
    private val applicationTaskExecutor: ThreadPoolTaskExecutor
) {
    companion object {
        private val log = logger()
        const val EVENT_BUS = "eventBus"
        const val ASYNC_EVENT_BUS = "asyncEventBus"
    }

    @Bean(EVENT_BUS)
    fun eventBus(): EventBus = EventBus("event-bus-${mafProjectProperties.projectArtifactId}").apply {
        log.warn("Initial bean: `${this.javaClass.simpleName}`")
    }

    @Bean(ASYNC_EVENT_BUS)
    fun asyncEventBus(): AsyncEventBus =
        AsyncEventBus("async-event-bus-${mafProjectProperties.projectArtifactId}", applicationTaskExecutor).apply {
            log.warn("Initial bean: `${this.javaClass.simpleName}`")
        }

    /**
     * On application ready event. org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'com.google.common.eventbus.EventBus' available: expected single matching bean but found 2: eventBus,asyncEventBus
     *
     * @param event
     */
    @Async
    @EventListener
    fun onApplicationReadyEvent(event: ApplicationReadyEvent) {
        log.info("All Beans have been initialized. Elapsed: ${event.timeTaken.seconds} s")
        val eventBus = applicationContext.getBean(EVENT_BUS, EventBus::class.java)
        val asyncEventBus = applicationContext.getBean(ASYNC_EVENT_BUS, AsyncEventBus::class.java)
        applicationContext.getBeansWithAnnotation(EventSubscriber::class.java).forEach { (key, value) ->
            eventBus.register(value)
            asyncEventBus.register(value)
            log.info("Event bus registered subscriber: $key")
        }
        eventBus.post(EventButReadyEvent())
        asyncEventBus.post(EventButReadyAsyncEvent())
    }
}
