package com.jmsoftware.maf.springcloudstarter.property

import com.jmsoftware.maf.common.util.logger
import jakarta.annotation.PostConstruct
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * # MafProjectProperties
 *
 * M&F project property, containing the basic constants of the project.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/13/22 2:54 PM
 */
@Validated
@Configuration
@Suppress("unused")
@ConfigurationProperties(prefix = MafProjectProperties.PREFIX)
class MafProjectProperties {
    companion object {
        const val PREFIX = "maf.project-properties"
        private val log = logger()
    }

    /**
     * The Base package.
     */
    lateinit var basePackage: @NotBlank String

    /**
     * The Context path.
     */
    lateinit var contextPath: String

    /**
     * The Group id.
     */
    lateinit var groupId: @NotBlank String

    /**
     * The Project parent artifact id.
     */
    lateinit var projectParentArtifactId: @NotBlank String

    /**
     * The Project artifact id.
     */
    lateinit var projectArtifactId: @NotBlank String

    /**
     * The Version.
     */
    lateinit var version: @NotBlank String

    /**
     * The Description.
     */
    lateinit var description: @NotBlank String

    /**
     * The Jdk version.
     */
    lateinit var jdkVersion: @NotBlank String

    /**
     * The Environment.
     */
    lateinit var environment: @NotBlank String

    /**
     * The Url.
     */
    lateinit var url: String

    /**
     * The Inception year.
     */
    lateinit var inceptionYear: String

    /**
     * The Organization name.
     */
    lateinit var organizationName: String

    /**
     * The Organization url.
     */
    lateinit var organizationUrl: String

    /**
     * The Issue management system.
     */
    lateinit var issueManagementSystem: String

    /**
     * The Issue management url.
     */
    lateinit var issueManagementUrl: String

    /**
     * The Developer name.
     */
    lateinit var developerName: String

    /**
     * The Developer email.
     */
    lateinit var developerEmail: String

    /**
     * The Developer url.
     */
    lateinit var developerUrl: String

    @PostConstruct
    fun postConstruct() {
        log.warn("Initial bean: `${this.javaClass.simpleName}`")
    }
}
