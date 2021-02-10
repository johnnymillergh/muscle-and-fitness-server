package com.jmsoftware.maf.exercisemis;

import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.StopWatch;

/**
 * <h1>ExerciseMisApplication</h1>
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
public class ExerciseMisApplication {
    private static final StopWatch STOP_WATCH = new StopWatch();
    private static SpringBootStartupHelper springBootStartupHelper;

    public ExerciseMisApplication(SpringBootStartupHelper springBootStartupHelper) {
        ExerciseMisApplication.springBootStartupHelper = springBootStartupHelper;
    }

    public static void main(String[] args) {
        STOP_WATCH.start();
        SpringApplication.run(ExerciseMisApplication.class, args);
        springBootStartupHelper.stop(STOP_WATCH);
    }
}
