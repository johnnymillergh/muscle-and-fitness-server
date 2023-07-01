package com.jmsoftware.maf.apigateway.property

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * # RedisRateLimiterConfigurationProperties
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:34 PM
 */
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = RedisRateLimiterConfigurationProperties.PREFIX)
class RedisRateLimiterConfigurationProperties {
    companion object {
        /**
         * The constant PREFIX.
         */
        const val PREFIX = "maf.configuration.redis-rate-limiter"
    }

    /**
     * The Replenish rate.
     */
    @NotBlank
    lateinit var replenishRate:  String

    /**
     * The Burst capacity.
     */
    @NotBlank
    lateinit var burstCapacity: String

    /**
     * The Requested tokens.
     */
    @NotBlank
    lateinit var requestedTokens: String
}
