package com.jmsoftware.maf.apigateway.configuration;

import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.maf.apigateway.property.RedisRateLimiterConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * Description: DiscoveryRouteConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 2/7/2021 1:15 PM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DiscoveryRouteConfiguration {
    public static final String REQUEST_RATE_LIMITER_FILTER_NAME = "RequestRateLimiter";
    /**
     * The <code>redis-rate-limiter.replenishRate</code> property is how many requests per second you want a user to
     * be allowed to do, without any dropped requests. This is the rate at which the token bucket is filled.
     */
    public static final String REPLENISH_RATE_KEY = "redis-rate-limiter.replenishRate";
    /**
     * The <code>redis-rate-limiter.burstCapacity</code> property is the maximum number of requests a user is allowed
     * to do in a single second. This is the number of tokens the token bucket can hold. Setting this value to zero
     * blocks all requests.
     */
    public static final String BURST_CAPACITY_KEY = "redis-rate-limiter.burstCapacity";
    /**
     * The <code>redis-rate-limiter.requestedTokens</code> property is how many tokens a request costs. This is the
     * number of tokens taken from the bucket for each request and defaults to <code>1</code>.
     */
    public static final String REQUESTED_TOKENS_KEY = "redis-rate-limiter.requestedTokens";
    private final DiscoveryLocatorProperties discoveryLocatorProperties;
    private final RedisRateLimiterConfigurationProperties redisRateLimiterConfigurationProperties;

    /**
     * Configure
     * <a href='https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-redis-ratelimiter'>The Redis <code>RateLimiter</code></a>.
     * <p>
     * The algorithm used is the <a href='https://en.wikipedia.org/wiki/Token_bucket'>Token Bucket Algorithm</a>.
     * <p>
     * Rate limits bellow <code>1 request/s</code> are accomplished by setting <code>replenishRate</code> to the
     * wanted number of requests, <code>requestedTokens</code> to the timespan in seconds and
     * <code>burstCapacity</code> to the product of <code>replenishRate</code> and <code>requestedTokens</code>, e.g.
     * setting <code>replenishRate=1</code>, <code>requestedTokens=60</code> and <code>burstCapacity=60</code> will
     * result in a limit of <code>1 request/min</code>.
     *
     * @see
     * <a href='https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-redis-ratelimiter'>The Redis RateLimiter</a>
     */
    @PostConstruct
    void postConstruct() {
        val filter = new FilterDefinition();
        filter.setName(REQUEST_RATE_LIMITER_FILTER_NAME);
        filter.addArg(REPLENISH_RATE_KEY, this.redisRateLimiterConfigurationProperties.getReplenishRate());
        filter.addArg(BURST_CAPACITY_KEY, this.redisRateLimiterConfigurationProperties.getBurstCapacity());
        filter.addArg(REQUESTED_TOKENS_KEY, this.redisRateLimiterConfigurationProperties.getRequestedTokens());
        this.discoveryLocatorProperties.getFilters().add(filter);
        log.info("Added filter [{}] for discovery services, filters: {}", REQUEST_RATE_LIMITER_FILTER_NAME,
                 this.discoveryLocatorProperties.getFilters());
    }

    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            val remoteAddress = exchange.getRequest().getRemoteAddress();
            if (ObjectUtil.isNotNull(remoteAddress)) {
                return Mono.just(remoteAddress.getHostName());
            }
            return Mono.just("unknown-address");
        };
    }
}
