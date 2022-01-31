package com.jmsoftware.maf.apigateway;

import com.jmsoftware.maf.reactivespringcloudstarter.helper.SpringBootStartupHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.util.StopWatch;

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
    private ApiGatewayApplication() {
    }

    public static void main(String[] args) {
        val stopWatch = new StopWatch();
        stopWatch.start();
        val configurableApplicationContext = SpringApplication.run(ApiGatewayApplication.class, args);
        val springBootStartupHelper = configurableApplicationContext.getBean(SpringBootStartupHelper.class);
        springBootStartupHelper.stop(stopWatch);
    }
}
