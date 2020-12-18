package com.jmsoftware.maf.springbootadmin;

import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.helper.IpHelper;
import com.jmsoftware.maf.springbootadmin.universal.configuration.ProjectProperty;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

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
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static ProjectProperty projectProperty;
    private static IpHelper ipHelper;

    public SpringBootAdminApplication(ProjectProperty projectProperty, IpHelper ipHelper) {
        SpringBootAdminApplication.projectProperty = projectProperty;
        SpringBootAdminApplication.ipHelper = ipHelper;
    }

    public static void main(String[] args) {
        val startInstant = Instant.now();
        SpringApplication.run(SpringBootAdminApplication.class, args);
        val endInstant = Instant.now();
        val duration = Duration.between(startInstant, endInstant);
        log.info("🥳 Congratulations! 🎉");
        log.info("🖥 {}@{} started!", projectProperty.getProjectArtifactId(), projectProperty.getVersion());
        log.info("⚙️ Environment: {}", projectProperty.getEnvironment());
        log.info("⏳ Deployment duration: {} seconds ({} ms)", duration.getSeconds(), duration.toMillis());
        log.info("⏰ App started at {} (timezone - {})", endInstant, TimeZone.getDefault().getDisplayName());
        log.info("{}  App running at{}  - Local:   http://localhost:{}{}/{}  - Network: http://{}:{}/{}",
                 LINE_SEPARATOR, LINE_SEPARATOR, ipHelper.getServerPort(), projectProperty.getContextPath(),
                 LINE_SEPARATOR, ipHelper.getPublicIp(), ipHelper.getServerPort(), projectProperty.getContextPath());
    }
}
