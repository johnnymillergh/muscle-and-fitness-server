package com.jmsoftware.maf.musclemis;

import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springcloudstarter.helper.IpHelper;
import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

/**
 * <h1>MuscleMisApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/17/20 8:41 PM
 **/
@Slf4j
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MuscleMisApplication {
    private static final StopWatch STOP_WATCH = new StopWatch();
    private static SpringBootStartupHelper springBootStartupHelper;

    public MuscleMisApplication(SpringBootStartupHelper springBootStartupHelper) {
        MuscleMisApplication.springBootStartupHelper = springBootStartupHelper;
    }

    public static void main(String[] args) {
        STOP_WATCH.start();
        SpringApplication.run(MuscleMisApplication.class, args);
        springBootStartupHelper.stop(STOP_WATCH);
    }
}
