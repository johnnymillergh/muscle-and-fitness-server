package com.jmsoftware.maf.authcenter.event

import com.google.common.eventbus.EventBus
import com.jmsoftware.maf.springcloudstarter.eventbus.EventSubscriber

/**
 * # EventTest
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/23/22 7:46 AM
 **/
@EventSubscriber
class EventTest(
    private val eventBus: EventBus
) {
}
