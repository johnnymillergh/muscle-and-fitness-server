package com.jmsoftware.maf.springbootadmin;

import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.StopWatch;

/**
 * <h1>SpringBootAdminApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com，date: 2/14/20 3:07 PM
 **/
@Slf4j
@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
@SuppressWarnings("scwjava_Createprivateconstructorforutilityclassallfieldsmethodsarestatic")
public class SpringBootAdminApplication {
    public static void main(String[] args) {
        val stopWatch = new StopWatch();
        stopWatch.start();
        val configurableApplicationContext = SpringApplication.run(SpringBootAdminApplication.class, args);
        val springBootStartupHelper = configurableApplicationContext.getBean(SpringBootStartupHelper.class);
        springBootStartupHelper.stop(stopWatch);
    }
}
