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
     * ## Algorithm
     *
     * The token bucket algorithm can be conceptually understood as follows:
     *
     * - A token is added to the bucket every 1 / r {\\displaystyle 1/r} ![1/r](https://wikimedia.org/api/rest_v1/media/math/render/svg/2ab96580d23ec5eff6bb0e666531eccb7a8035d6) seconds.
     * - The bucket can hold at the most b {\\displaystyle b} ![b](https://wikimedia.org/api/rest_v1/media/math/render/svg/f11423fbb2e967f986e36804a8ae4271734917c3) tokens. If a token arrives when the bucket is full, it is discarded.
     * - When a packet (network layer [PDU](https://en.wikipedia.org/wiki/Protocol_data_unit "Protocol data unit")) of _n_ bytes arrives,
     * - if at least _n_ tokens are in the bucket, _n_ tokens are removed from the bucket, and the packet is sent to the network.
     * - if fewer than _n_ tokens are available, no tokens are removed from the bucket, and the packet is considered to be _non-conformant_.
     *
     * ### Variations
     *
     * Implementers of this algorithm on platforms lacking the clock resolution necessary to add a single token to the bucket every 1 / r {\\displaystyle 1/r} ![1/r](https://wikimedia.org/api/rest_v1/media/math/render/svg/2ab96580d23ec5eff6bb0e666531eccb7a8035d6) seconds may want to consider an alternative formulation. Given the ability to update the token bucket every S milliseconds, the number of tokens to add every S milliseconds = ( r ∗ S )  /  1000   {\\displaystyle (r\*S)/1000} ![(r*S)/1000](https://wikimedia.org/api/rest_v1/media/math/render/svg/8346b25098bc785ea08018e719e9073e308d1bed) .
     *
     * ### Properties
     *
     * #### Average rate
     *
     * Over the long run the output of conformant packets is limited by the token rate, r {\\displaystyle r} ![r](https://wikimedia.org/api/rest_v1/media/math/render/svg/0d1ecb613aa2984f0576f70f86650b7c2a132538) .
     *
     * #### Burst size
     *
     * Let M {\\displaystyle M} ![M](https://wikimedia.org/api/rest_v1/media/math/render/svg/f82cade9898ced02fdd08712e5f0c0151758a0dd) be the maximum possible transmission rate in bytes/second.
     *
     * Then T max \= { b / ( M − r ) if r < M ∞ otherwise {\\displaystyle T\_{\\text{max}}={\\begin{cases}b/(M-r)&{\\text{ if }}r<M\\\\\\infty &{\\text{ otherwise }}\\end{cases}}} ![T_{{\text{max}}}={\begin{cases}b/(M-r)&{\text{ if }}r<M\\\infty &{\text{ otherwise }}\end{cases}}](https://wikimedia.org/api/rest_v1/media/math/render/svg/e7fce5b7de61b4f43e6c6df9654acfaf1a81c425) is the maximum burst time, that is the time for which the rate  M   {\\displaystyle M} ![M](https://wikimedia.org/api/rest_v1/media/math/render/svg/f82cade9898ced02fdd08712e5f0c0151758a0dd) is fully utilized.
     *
     * The maximum burst size is thus B max \= T max ∗ M {\\displaystyle B\_{\\text{max}}=T\_{\\text{max}}\*M} ![{\displaystyle B_{\text{max}}=T_{\text{max}}*M}](https://wikimedia.org/api/rest_v1/media/math/render/svg/0e9c6b65e67d65dc2da82aa5c6dad401ea9c0a20)
     *
     * ### Uses
     *
     * The token bucket can be used in either [traffic shaping](https://en.wikipedia.org/wiki/Traffic_shaping "Traffic shaping") or [traffic policing](https://en.wikipedia.org/wiki/Traffic_policing_(communications) "Traffic policing (communications)"). In traffic policing, nonconforming packets may be discarded (dropped) or may be reduced in priority (for downstream traffic management functions to drop if there is congestion). In traffic shaping, packets are delayed until they conform. Traffic policing and traffic shaping are commonly used to protect the network against excess or excessively bursty traffic, see [bandwidth management](https://en.wikipedia.org/wiki/Bandwidth_management "Bandwidth management") and [congestion avoidance](https://en.wikipedia.org/wiki/Congestion_avoidance "Congestion avoidance"). Traffic shaping is commonly used in the [network interfaces](https://en.wikipedia.org/wiki/Network_interface_controller "Network interface controller") in [hosts](https://en.wikipedia.org/wiki/Host_(network) "Host (network)") to prevent transmissions being discarded by traffic management functions in the network.
     *
     * @see <a href='https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-redis-ratelimiter'>The Redis RateLimiter</a>
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
