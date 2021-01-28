package com.jmsoftware.maf.apigateway;

import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.reactivespringbootstarter.helper.IpHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static MafProjectProperty mafProjectProperty;
    private static IpHelper ipHelper;

    public ApiGatewayApplication(MafProjectProperty mafProjectProperty, IpHelper ipHelper) {
        ApiGatewayApplication.mafProjectProperty = mafProjectProperty;
        ApiGatewayApplication.ipHelper = ipHelper;
    }

    public static void main(String[] args) {
        val startInstant = Instant.now();
        SpringApplication.run(ApiGatewayApplication.class, args);
        val endInstant = Instant.now();
        val duration = Duration.between(startInstant, endInstant);
        log.info("🥳 Congratulations! 🎉");
        log.info("🖥 {}@{} started!", mafProjectProperty.getProjectArtifactId(), mafProjectProperty.getVersion());
        log.info("⚙️ Environment: {}", mafProjectProperty.getEnvironment());
        log.info("⏳ Deployment duration: {} seconds ({} ms)", duration.getSeconds(), duration.toMillis());
        log.info("⏰ App started at {} (timezone - {})", endInstant, TimeZone.getDefault().getDisplayName());
        log.info("{}  App running at{}  - Local:   http://localhost:{}{}/{}  - Network: {}/{}",
                 LINE_SEPARATOR, LINE_SEPARATOR, ipHelper.getServerPort(), mafProjectProperty.getContextPath(),
                 LINE_SEPARATOR, ipHelper.getBaseUrl(), mafProjectProperty.getContextPath());
    }
}
