package com.jmsoftware.maf.apigateway

import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.util.StopWatch

/**
 * <h1>ApiGatewayApplication</h1>
 *
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/22/2020 3:39 PM
 */
@EnableDiscoveryClient
@SpringBootApplication
class ApiGatewayApplication

fun main(args: Array<String>) {
    StopWatch().let {
        it.start()
        runApplication<ApiGatewayApplication>(*args)
            .getBean(SpringBootStartupHelper::class.java)
            .stop(it)
    }
}

