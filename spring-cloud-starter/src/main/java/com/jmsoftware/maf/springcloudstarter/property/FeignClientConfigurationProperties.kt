package com.jmsoftware.maf.springcloudstarter.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * # FeignClientConfigurationProperties
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 9:40 PM
 */
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = FeignClientConfigurationProperties.PREFIX)
class FeignClientConfigurationProperties {
    companion object {
        const val PREFIX = "feign.client.config.default"
    }

    /**
     * Enabled AOP log for Feign clients. Default is true. True means enabled, false means disabled.
     */
    var enabledAopLog = true
}
