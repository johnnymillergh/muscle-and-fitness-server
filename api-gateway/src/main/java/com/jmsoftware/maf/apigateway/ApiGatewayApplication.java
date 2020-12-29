package com.jmsoftware.maf.apigateway;

import com.jmsoftware.maf.apigateway.universal.configuration.ServerConfiguration;
import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafProjectProperty;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactivefeign.spring.config.EnableReactiveFeignClients;

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
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveFeignClients
public class ApiGatewayApplication {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static MafProjectProperty mafProjectProperty;
    private static ServerConfiguration serverConfiguration;

    public ApiGatewayApplication(MafProjectProperty mafProjectProperty, ServerConfiguration serverConfiguration) {
        ApiGatewayApplication.mafProjectProperty = mafProjectProperty;
        ApiGatewayApplication.serverConfiguration = serverConfiguration;
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
                 LINE_SEPARATOR, LINE_SEPARATOR, serverConfiguration.getServerPort(), mafProjectProperty.getContextPath(),
                 LINE_SEPARATOR, serverConfiguration.getBaseUrl(), mafProjectProperty.getContextPath());
    }
}
