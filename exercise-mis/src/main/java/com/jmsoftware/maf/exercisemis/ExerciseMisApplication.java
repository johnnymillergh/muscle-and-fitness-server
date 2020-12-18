package com.jmsoftware.maf.exercisemis;

import com.jmsoftware.maf.exercisemis.universal.configuration.ProjectProperty;
import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.helper.IpHelper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;

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
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static ProjectProperty projectProperty;
    private static IpHelper ipHelper;

    public ExerciseMisApplication(ProjectProperty projectProperty, IpHelper ipHelper) {
        ExerciseMisApplication.projectProperty = projectProperty;
        ExerciseMisApplication.ipHelper = ipHelper;
    }

    public static void main(String[] args) {
        val startInstant = Instant.now();
        SpringApplication.run(ExerciseMisApplication.class, args);
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
