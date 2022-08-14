package com.jmsoftware.maf.mafmis

import com.jmsoftware.maf.springcloudstarter.eventbus.EnableEventBus
import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.util.StopWatch

/**
 * # MafMisApplication
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:23 PM
 */
@EnableEventBus
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
class MafMisApplication

fun main(args: Array<String>) {
    StopWatch().let {
        it.start()
        runApplication<MafMisApplication>(*args)
            .getBean(SpringBootStartupHelper::class.java)
            .stop(it)
    }
}
