package com.jmsoftware.maf.springbootadmin

import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper
import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.util.StopWatch

/**
 * # SpringBootAdminApplication
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:40 AM
 */
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
class SpringBootAdminApplication

fun main(args: Array<String>) {
    val stopWatch = StopWatch()
    stopWatch.start()
    runApplication<SpringBootAdminApplication>(*args)
        .getBean(SpringBootStartupHelper::class.java)
        .stop(stopWatch)
}
