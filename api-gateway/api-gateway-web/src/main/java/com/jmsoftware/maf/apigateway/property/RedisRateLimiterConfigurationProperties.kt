package com.jmsoftware.maf.apigateway.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * <h1>RedisRateLimiterConfigurationProperties</h1>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 3/1/2021 9:59 AM
 */
@Data
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = RedisRateLimiterConfigurationProperties.PREFIX)
public class RedisRateLimiterConfigurationProperties {
    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "maf.configuration.redis-rate-limiter";
    /**
     * The Replenish rate.
     */
    @NotBlank
    private String replenishRate;
    /**
     * The Burst capacity.
     */
    @NotBlank
    private String burstCapacity;
    /**
     * The Requested tokens.
     */
    @NotBlank
    private String requestedTokens;
}
