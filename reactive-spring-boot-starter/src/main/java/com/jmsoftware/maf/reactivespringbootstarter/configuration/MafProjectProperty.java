package com.jmsoftware.maf.reactivespringbootstarter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * <h1>MafProjectProperty</h1>
 * <p>
 * M&F project property, containing the basic constants of the project.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 1:21 PM
 */
@Data
@Validated
@Component
@SuppressWarnings("jol")
@ConfigurationProperties(prefix = "maf.project-property")
public class MafProjectProperty {
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
}
