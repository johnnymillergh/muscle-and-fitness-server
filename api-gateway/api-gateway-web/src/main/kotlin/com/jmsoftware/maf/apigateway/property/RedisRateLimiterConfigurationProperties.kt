package com.jmsoftware.maf.apigateway.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

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
    lateinit var replenishRate: @NotBlank String

    /**
     * The Burst capacity.
     */
    lateinit var burstCapacity: @NotBlank String

    /**
     * The Requested tokens.
     */
    lateinit var requestedTokens: @NotBlank String
}
