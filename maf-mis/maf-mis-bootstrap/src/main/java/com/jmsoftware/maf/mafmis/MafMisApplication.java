package com.jmsoftware.maf.mafmis;

import com.jmsoftware.maf.springcloudstarter.helper.SpringBootStartupHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.StopWatch;

/**
 * <h1>MafMisApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/25/2021 4:23 PM
 **/
@Slf4j
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@SuppressWarnings("scwjava_Createprivateconstructorforutilityclassallfieldsmethodsarestatic")
public class MafMisApplication {
    public static void main(String[] args) {
        val stopWatch = new StopWatch();
        stopWatch.start();
        val configurableApplicationContext = SpringApplication.run(MafMisApplication.class, args);
        val springBootStartupHelper = configurableApplicationContext.getBean(SpringBootStartupHelper.class);
        springBootStartupHelper.stop(stopWatch);
    }
}
