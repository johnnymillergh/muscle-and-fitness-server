package com.jmsoftware.maf.springcloudstarter.quartz;

import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Description: GreetingQuartzJobBean, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/13/2021 10:16 PM
 * @see
 * <a href='https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.quartz'>Quartz Scheduler</a>
 **/
@Slf4j
@RequiredArgsConstructor
public class GreetingQuartzJobBean extends QuartzJobBean {
    /**
     * Injected field, spring bean.
     */
    private final MafProjectProperty mafProjectProperty;

    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        log.info("Greeting from Quartz job, current service is: {}", mafProjectProperty.getProjectArtifactId());
    }
}
