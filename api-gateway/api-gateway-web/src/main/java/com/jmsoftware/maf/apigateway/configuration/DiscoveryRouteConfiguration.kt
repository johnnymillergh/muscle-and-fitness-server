package com.jmsoftware.maf.apigateway.configuration

import com.jmsoftware.maf.apigateway.property.RedisRateLimiterConfigurationProperties
import com.jmsoftware.maf.common.util.logger
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties
import org.springframework.cloud.gateway.filter.FilterDefinition
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

/**
 * # DiscoveryRouteConfiguration
 *
 * Description: DiscoveryRouteConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:19 PM
 */
@Configuration
class DiscoveryRouteConfiguration(
    private val discoveryLocatorProperties: DiscoveryLocatorProperties,
    private val redisRateLimiterConfigurationProperties: RedisRateLimiterConfigurationProperties
) {
    companion object {
        private val log = logger()
        const val REQUEST_RATE_LIMITER_FILTER_NAME = "RequestRateLimiter"

        /**
         * The `redis-rate-limiter.replenishRate` property is how many requests per second you want a user to
         * be allowed to do, without any dropped requests. This is the rate at which the token bucket is filled.
         */
        const val REPLENISH_RATE_KEY = "redis-rate-limiter.replenishRate"

        /**
         * The `redis-rate-limiter.burstCapacity` property is the maximum number of requests a user is allowed
         * to do in a single second. This is the number of tokens the token bucket can hold. Setting this value to zero
         * blocks all requests.
         */
        const val BURST_CAPACITY_KEY = "redis-rate-limiter.burstCapacity"

        /**
         * The `redis-rate-limiter.requestedTokens` property is how many tokens a request costs. This is the
         * number of tokens taken from the bucket for each request and defaults to `1`.
         */
        const val REQUESTED_TOKENS_KEY = "redis-rate-limiter.requestedTokens"
    }

    /**
     * Configure
     * [The Redis `RateLimiter`](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-redis-ratelimiter).
     *
     *
     * The algorithm used is the [Token Bucket Algorithm](https://en.wikipedia.org/wiki/Token_bucket).
     *
     *
     * Rate limits bellow `1 request/s` are accomplished by setting `replenishRate` to the
     * wanted number of requests, `requestedTokens` to the timespan in seconds and
     * `burstCapacity` to the product of `replenishRate` and `requestedTokens`, e.g.
     * setting `replenishRate=1`, `requestedTokens=60` and `burstCapacity=60` will
     * result in a limit of `1 request/min`.
     *
     * @see  [The Redis RateLimiter](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/.the-redis-ratelimiter)
     */
    @PostConstruct
    fun postConstruct() {
        val filter = FilterDefinition()
        filter.name = REQUEST_RATE_LIMITER_FILTER_NAME
        filter.addArg(REPLENISH_RATE_KEY, redisRateLimiterConfigurationProperties.replenishRate)
        filter.addArg(BURST_CAPACITY_KEY, redisRateLimiterConfigurationProperties.burstCapacity)
        filter.addArg(REQUESTED_TOKENS_KEY, redisRateLimiterConfigurationProperties.requestedTokens)
        discoveryLocatorProperties.filters.add(filter)
        log.info("Added filter [$REQUEST_RATE_LIMITER_FILTER_NAME] for discovery services, filters: ${discoveryLocatorProperties.filters}")
    }

    @Bean
    @Primary
    fun ipKeyResolver(): KeyResolver {
        return KeyResolver { exchange: ServerWebExchange ->
            val remoteAddress = exchange.request.remoteAddress
            if (remoteAddress != null) {
                Mono.just(remoteAddress.hostName)
            } else {
                Mono.just("unknown-address")
            }
        }
    }
}
