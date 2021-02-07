package com.jmsoftware.maf.authcenter;

import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springcloudstarter.helper.IpHelper;
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
 * <h1>AuthCenterApplication</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 3/12/20 9:57 AM
 **/
@Slf4j
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class AuthCenterApplication {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static MafProjectProperty mafProjectProperty;
    private static IpHelper ipHelper;

    public AuthCenterApplication(MafProjectProperty projectProperty, IpHelper ipHelper) {
        AuthCenterApplication.mafProjectProperty = projectProperty;
        AuthCenterApplication.ipHelper = ipHelper;
    }

    public static void main(String[] args) {
        val startInstant = Instant.now();
        SpringApplication.run(AuthCenterApplication.class, args);
        val endInstant = Instant.now();
        val duration = Duration.between(startInstant, endInstant);
        log.info("🥳 Congratulations! 🎉");
        log.info("🖥 {}@{} started!", mafProjectProperty.getProjectArtifactId(), mafProjectProperty.getVersion());
        log.info("⚙️ Environment: {}", mafProjectProperty.getEnvironment());
        log.info("⏳ Deployment duration: {} seconds ({} ms)", duration.getSeconds(), duration.toMillis());
        log.info("⏰ App started at {} (timezone - {})", endInstant, TimeZone.getDefault().getDisplayName());
        log.info("{}  App running at{}  - Local:   http://localhost:{}{}/{}  - Network: http://{}:{}{}/",
                 LINE_SEPARATOR, LINE_SEPARATOR, ipHelper.getServerPort(), mafProjectProperty.getContextPath(),
                 LINE_SEPARATOR, ipHelper.getPublicIp(), ipHelper.getServerPort(), mafProjectProperty.getContextPath());
    }
}
