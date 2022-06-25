package com.jmsoftware.maf.authcenter.event

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.eventbus.DemoEvent
import com.jmsoftware.maf.springcloudstarter.eventbus.EventSubscriber

/**
 * # DemoEventSubscriber
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/23/22 7:46 AM
 **/
@EventSubscriber
@Suppress("unused")
class DemoEventSubscriber(
    private val eventBus: EventBus
) {
    companion object {
        private val log = logger()
    }

    @Subscribe
    @AllowConcurrentEvents
    fun subscribe(demoEvent: DemoEvent) {
        log.info("Got subscription: $demoEvent")
    }
}
