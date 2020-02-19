package com.jmsoftware.gateway.universal.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <h1>ProjectProperty</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-04-18 13:01
 **/
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "project.property")
public class ProjectProperty {
    private String basePackage;
    private String contextPath;
    private String groupId;
    private String artifactId;
    private String projectArtifactId;
    private String version;
    private String description;
    private String jdkVersion;
    private String environment;
    private String environmentAlias;
    private String url;
    private String inceptionYear;
    private String organizationName;
    private String organizationUrl;
    private String issueManagementSystem;
    private String issueManagementUrl;
    private String developerName;
    private String developerEmail;
    private String developerUrl;
}
