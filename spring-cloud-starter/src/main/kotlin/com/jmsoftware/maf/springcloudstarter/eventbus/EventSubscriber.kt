package com.jmsoftware.maf.springcloudstarter.eventbus

import org.springframework.stereotype.Component
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * # EventSubscriber
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/23/22 7:52 AM
 **/
@Component
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
annotation class EventSubscriber
