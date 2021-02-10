package com.jmsoftware.maf.apigateway;

import com.jmsoftware.maf.reactivespringcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.IpHelper;
import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

/**
 * <h1>ApiGatewayApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/22/2020 3:39 PM
 **/
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {
    private static final StopWatch STOP_WATCH = new StopWatch();
    private static SpringBootStartupHelper springBootStartupHelper;

    public ApiGatewayApplication(SpringBootStartupHelper springBootStartupHelper) {
        ApiGatewayApplication.springBootStartupHelper = springBootStartupHelper;
    }

    public static void main(String[] args) {
        STOP_WATCH.start();
        SpringApplication.run(ApiGatewayApplication.class, args);
        springBootStartupHelper.stop(STOP_WATCH);
    }
}
