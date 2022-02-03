package com.jmsoftware.maf.springcloudstarter.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * <h1>FeignClientConfigurationProperties</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/1/22 6:03 PM
 **/
@Data
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = FeignClientConfigurationProperties.PREFIX)
public class FeignClientConfigurationProperties {
    public static final String PREFIX = "feign.client.config.default";
    /**
     * Enabled AOP log for Feign clients. Default is true. True means enabled, false means disabled.
     */
    @NotNull
    private Boolean enabledAopLog = Boolean.TRUE;
}
