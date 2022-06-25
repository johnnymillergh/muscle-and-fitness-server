package com.jmsoftware.maf.springcloudstarter.eventbus

import org.springframework.context.annotation.*
import org.springframework.core.annotation.AliasFor
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*


/**
 * # EnableEventBus
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/23/22 7:52 AM
 **/
@Target(CLASS)
@Retention(RUNTIME)
@MustBeDocumented
@Import(
    EventSubscriberRegistrar::class,
    EventBugConfiguration::class
)
annotation class EnableEventBus(
    @get:AliasFor(attribute = "basePackages") val value: Array<String> = [],
    @get:AliasFor(attribute = "value") val basePackages: Array<String> = []
)
