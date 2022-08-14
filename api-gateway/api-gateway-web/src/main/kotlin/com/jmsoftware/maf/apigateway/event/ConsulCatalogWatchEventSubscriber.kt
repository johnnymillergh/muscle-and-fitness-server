package com.jmsoftware.maf.apigateway.event

import cn.hutool.extra.spring.SpringUtil
import com.jmsoftware.maf.apigateway.configuration.OpenApiConfiguration
import com.jmsoftware.maf.apigateway.configuration.OpenApiConfiguration.Companion.GROUPED_OPEN_API_LIST_NAME
import com.jmsoftware.maf.common.util.logger
import org.springframework.cloud.client.discovery.event.HeartbeatEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference

/**
 * # ConsulCatalogWatchEventSubscriber
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/26/22 9:35 AM
 **/
@Component
class ConsulCatalogWatchEventSubscriber(
    private val openApiConfiguration: OpenApiConfiguration
) {
    private val consulIndex: AtomicReference<Long> = AtomicReference(0L)

    companion object {
        private val log = logger()
    }

    @EventListener
    fun onApplicationEvent(heartbeatEvent: HeartbeatEvent) {
        val newValue = heartbeatEvent.value as Long
        if (consulIndex.get().equals(newValue)) {
            return
        }
        consulIndex.set(newValue)
        log.info("Consul index has changed, consulIndex: ${consulIndex.get()}")
        SpringUtil.unregisterBean(GROUPED_OPEN_API_LIST_NAME)
        log.warn("Unregistered bean groupedOpenApiList")
        SpringUtil.registerBean(GROUPED_OPEN_API_LIST_NAME, openApiConfiguration.createGroupedOpenApiList())
        log.warn("Re-registered bean groupedOpenApiList")
    }
}
