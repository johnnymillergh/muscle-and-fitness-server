package com.jmsoftware.demotwo;

import com.jmsoftware.demotwo.universal.configuration.ProjectProperty;
import com.jmsoftware.demotwo.universal.configuration.ServerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;


/**
 * <h1>DemoTwoApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/15/20 10:32 PM
 **/
@Slf4j
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class DemoTwoApplication {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static ProjectProperty projectProperty;
    private static ServerConfiguration serverConfiguration;

    public DemoTwoApplication(ProjectProperty projectProperty, ServerConfiguration serverConfiguration) {
        DemoTwoApplication.projectProperty = projectProperty;
        DemoTwoApplication.serverConfiguration = serverConfiguration;
    }

    public static void main(String[] args) {
        var startInstant = Instant.now();
        SpringApplication.run(DemoTwoApplication.class, args);
        var endInstant = Instant.now();
        var duration = Duration.between(startInstant, endInstant);
        log.info("🥳 Congratulations! 🎉");
        log.info("🖥 {}@{} started!", projectProperty.getProjectArtifactId(), projectProperty.getVersion());
        log.info("⚙️ Environment: {} ({})", projectProperty.getEnvironment(), projectProperty.getEnvironmentAlias());
        log.info("⏳ Deployment duration: {} seconds ({} ms)", duration.getSeconds(), duration.toMillis());
        log.info("⏰ App started at {} (timezone - {})", endInstant, TimeZone.getDefault().getDisplayName());
        log.info("{}  App running at{}  - Local:   http://localhost:{}{}/{}  - Network: {}/",
                 LINE_SEPARATOR, LINE_SEPARATOR, serverConfiguration.getServerPort(), projectProperty.getContextPath(),
                 LINE_SEPARATOR, serverConfiguration.getBaseUrl());
    }
}
