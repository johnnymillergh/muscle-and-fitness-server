package com.jmsoftware.maf.authcenter

import com.jmsoftware.maf.springcloudstarter.eventbus.EnableEventBus
import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.util.StopWatch

/**
 * # AuthCenterApplication
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/10/22 11:44 AM
 */
@EnableEventBus
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@ConfigurationPropertiesScan
class AuthCenterApplication

fun main(args: Array<String>) {
    val stopWatch = StopWatch()

    stopWatch.start()
    runApplication<AuthCenterApplication>(*args)
        .getBean(SpringBootStartupHelper::class.java)
        .stop(stopWatch)
}
