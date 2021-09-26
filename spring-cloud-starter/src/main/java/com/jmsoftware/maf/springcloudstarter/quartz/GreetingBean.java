package com.jmsoftware.maf.springcloudstarter.quartz;

import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springcloudstarter.quartz.annotation.QuartzSchedulable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description: GreetingBean, demo for dynamic Quartz job, which is configured in the table `quartz_job_configuration`.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:16 PM
 * @see
 * <a href='https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.quartz'>Quartz Scheduler</a>
 **/
@Slf4j
@Component("greetingBean")
@RequiredArgsConstructor
public class GreetingBean {
    /**
     * Injected field, spring bean.
     */
    private final MafProjectProperty mafProjectProperty;

    @QuartzSchedulable
    public void hello() {
        log.info("Greeting from Quartz job, current service is: {}", this.mafProjectProperty.getProjectArtifactId());
    }
}
