package com.jmsoftware.maf.osscenter

import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.util.StopWatch

/**
 * # OssCenterApplication
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 8:47 AM
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
class OssCenterApplication

fun main(args: Array<String>) {
    StopWatch().let {
        it.start()
        runApplication<OssCenterApplication>(*args)
            .getBean(SpringBootStartupHelper::class.java)
            .stop(it)
    }
}
