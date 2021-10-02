package com.jmsoftware.maf.springcloudstarter.property;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

/**
 * <h1>MafProjectProperties</h1>
 * <p>
 * M&F project property, containing the basic constants of the project.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 12:57 PM
 */
@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = MafProjectProperties.PREFIX)
public class MafProjectProperties {
    public static final String PREFIX = "maf.project-properties";
    private final BuildProperties buildProperties;
    /**
     * The Base package.
     */
    @NotBlank
    private String basePackage;
    /**
     * The Context path.
     */
    private String contextPath;
    /**
     * The Group id.
     */
    @NotBlank
    private String groupId;
    /**
     * The Project parent artifact id.
     */
    @NotBlank
    private String projectParentArtifactId;
    /**
     * The Project artifact id.
     */
    @NotBlank
    private String projectArtifactId;
    /**
     * The Version.
     */
    @NotBlank
    private String version;
    /**
     * The Description.
     */
    @NotBlank
    private String description;
    /**
     * The Jdk version.
     */
    @NotBlank
    private String jdkVersion;
    /**
     * The Environment.
     */
    @NotBlank
    private String environment;
    /**
     * The Url.
     */
    private String url;
    /**
     * The Inception year.
     */
    private String inceptionYear;
    /**
     * The Organization name.
     */
    private String organizationName;
    /**
     * The Organization url.
     */
    private String organizationUrl;
    /**
     * The Issue management system.
     */
    private String issueManagementSystem;
    /**
     * The Issue management url.
     */
    private String issueManagementUrl;
    /**
     * The Developer name.
     */
    private String developerName;
    /**
     * The Developer email.
     */
    private String developerEmail;
    /**
     * The Developer url.
     */
    private String developerUrl;

    @PostConstruct
    private void postConstruct() {
        log.warn("Initial bean: '{}'", this.getClass().getSimpleName());
    }
}
