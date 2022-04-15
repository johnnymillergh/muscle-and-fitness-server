package com.jmsoftware.maf.springcloudstarter.quartz

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.quartz.annotation.QuartzSchedulable
import org.springframework.stereotype.Component

/**
 * Description: GreetingBean, demo for dynamic Quartz job, which is configured in the table `quartz_job_configuration`.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:16 PM
 * @see  [Quartz Scheduler](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html.features.quartz)
 */
@Component("greetingBean")
class GreetingBean(
    private val mafProjectProperties: MafProjectProperties
) {
    companion object {
        val log = logger()
    }

    @QuartzSchedulable
    @Suppress("unused")
    fun hello() {
        log.info(
            "Greeting from Quartz job, current service is: {}",
            mafProjectProperties!!.projectArtifactId
        )
    }
}
