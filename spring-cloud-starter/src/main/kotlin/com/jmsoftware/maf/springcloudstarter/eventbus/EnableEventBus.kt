package com.jmsoftware.maf.springcloudstarter.eventbus

import org.springframework.context.annotation.Import

/**
 * # EnableEventBus
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/23/22 7:52 AM
 **/
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(EventBugConfiguration::class)
annotation class EnableEventBus
