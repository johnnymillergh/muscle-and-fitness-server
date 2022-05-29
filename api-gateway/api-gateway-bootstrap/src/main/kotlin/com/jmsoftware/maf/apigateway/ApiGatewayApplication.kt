package com.jmsoftware.maf.apigateway

import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.util.StopWatch

/**
 * # ApiGatewayApplication
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 8:48 AM
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

