package com.jmsoftware.maf.springbootadmin;

import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.StopWatch;

/**
 * <h1>SpringBootAdminApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/14/20 3:07 PM
 **/
@Slf4j
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class SpringBootAdminApplication {
    private static final StopWatch STOP_WATCH = new StopWatch();
    private static SpringBootStartupHelper springBootStartupHelper;

    public SpringBootAdminApplication(SpringBootStartupHelper springBootStartupHelper) {
        SpringBootAdminApplication.springBootStartupHelper = springBootStartupHelper;
    }

    public static void main(String[] args) {
        STOP_WATCH.start();
        SpringApplication.run(SpringBootAdminApplication.class, args);
        springBootStartupHelper.stop(STOP_WATCH);
    }
}
