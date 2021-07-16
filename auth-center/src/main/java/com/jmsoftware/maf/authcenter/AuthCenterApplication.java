package com.jmsoftware.maf.authcenter;

import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.StopWatch;

/**
 * <h1>AuthCenterApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 3/12/20 9:57 AM
 **/
@Slf4j
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthCenterApplication {
    private static final StopWatch STOP_WATCH = new StopWatch();
    private static SpringBootStartupHelper springBootStartupHelper;

    public AuthCenterApplication(SpringBootStartupHelper springBootStartupHelper) {
        AuthCenterApplication.springBootStartupHelper = springBootStartupHelper;
    }

    public static void main(String[] args) {
        STOP_WATCH.start();
        SpringApplication.run(AuthCenterApplication.class, args);
        springBootStartupHelper.stop(STOP_WATCH);
    }
}
