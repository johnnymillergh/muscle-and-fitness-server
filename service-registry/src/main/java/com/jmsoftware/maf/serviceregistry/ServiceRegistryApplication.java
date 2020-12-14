package com.jmsoftware.maf.serviceregistry;

import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.helper.IpHelper;
import com.jmsoftware.maf.serviceregistry.universal.configuration.ProjectProperty;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

/**
 * <h1>ServiceRegistryApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/14/20 10:56 AM
 **/
@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class ServiceRegistryApplication {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static ProjectProperty projectProperty;
    private static IpHelper ipHelper;

    public ServiceRegistryApplication(ProjectProperty projectProperty, IpHelper ipHelper) {
        ServiceRegistryApplication.projectProperty = projectProperty;
        ServiceRegistryApplication.ipHelper = ipHelper;
    }

    public static void main(String[] args) {
        val startInstant = Instant.now();
        SpringApplication.run(ServiceRegistryApplication.class, args);
        val endInstant = Instant.now();
        val duration = Duration.between(startInstant, endInstant);
        log.info("🥳 Congratulations! 🎉");
        log.info("🖥 {}@{} started!", projectProperty.getProjectArtifactId(), projectProperty.getVersion());
        log.info("⚙️ Environment: {}", projectProperty.getEnvironment());
        log.info("⏳ Deployment duration: {} seconds ({} ms)", duration.getSeconds(), duration.toMillis());
        log.info("⏰ App started at {} (timezone - {})", endInstant, TimeZone.getDefault().getDisplayName());
        log.info("{}  App running at{}  - Local:   http://localhost:{}{}/{}  - Network: http://{}/{}",
                 LINE_SEPARATOR, LINE_SEPARATOR, ipHelper.getServerPort(), projectProperty.getContextPath(),
                 LINE_SEPARATOR, ipHelper.getPublicIp(), projectProperty.getContextPath());
    }
}
