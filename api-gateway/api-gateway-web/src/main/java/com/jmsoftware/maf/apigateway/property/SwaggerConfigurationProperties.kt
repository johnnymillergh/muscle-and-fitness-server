package com.jmsoftware.maf.apigateway.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

/**
 * # SwaggerConfigurationProperties
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:17 PM
 */
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = SwaggerConfigurationProperties.PREFIX)
class SwaggerConfigurationProperties {
    companion object {
        const val PREFIX = "maf.configuration.swagger"
    }

    /**
     * Ignored service id set
     */
    lateinit var ignoredServiceIds: @NotEmpty Set<String>
}
