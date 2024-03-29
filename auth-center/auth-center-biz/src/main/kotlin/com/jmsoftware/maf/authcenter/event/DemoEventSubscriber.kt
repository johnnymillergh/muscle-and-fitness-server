package com.jmsoftware.maf.authcenter.event

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.eventbus.EventButReadyAsyncEvent
import com.jmsoftware.maf.springcloudstarter.eventbus.EventButReadyEvent
import com.jmsoftware.maf.springcloudstarter.eventbus.EventSubscriber
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties

/**
 * # DemoEventSubscriber
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/23/22 7:46 AM
 **/
@EventSubscriber
class DemoEventSubscriber(
    private val mafProjectProperties: MafProjectProperties
) {
    companion object {
        private val log = logger()
    }

    @Subscribe
    @Suppress("unused")
    fun subscribeNonAsyncEvent(event: EventButReadyEvent) {
        log.info("${mafProjectProperties.projectArtifactId}.${this.javaClass.simpleName} got non-async subscription of: $event")
    }

    @Subscribe
    @AllowConcurrentEvents
    @Suppress("unused")
    fun subscribeAsyncEvent(event: EventButReadyAsyncEvent) {
        log.info("${mafProjectProperties.projectArtifactId}.${this.javaClass.simpleName} got async subscription of: $event")
    }
}
