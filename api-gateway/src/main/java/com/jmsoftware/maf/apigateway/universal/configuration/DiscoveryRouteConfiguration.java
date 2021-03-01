package com.jmsoftware.maf.apigateway.universal.configuration;

import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public final String FILTER_NAME = "RequestRateLimiter";
    public final String REPLENISH_RATE_KEY = "redis-rate-limiter.replenishRate";
    public final String BURST_CAPACITY_KEY = "redis-rate-limiter.burstCapacity";
    public final String REQUESTED_TOKENS_KEY = "redis-rate-limiter.requestedTokens";
    private final DiscoveryLocatorProperties discoveryLocatorProperties;
    private final RedisRateLimiterConfiguration redisRateLimiterConfiguration;

    /**
     * Post construct.
     *
     * @see
     * <a href='https://docs.spring.io/spring-cloud-gateway/docs/3.0.1/reference/html/#the-redis-ratelimiter'>The Redis RateLimiter</a>
     */
    @PostConstruct
    void postConstruct() {
        val filter = new FilterDefinition();
        filter.setName(FILTER_NAME);
        // Setting replenishRate=1, requestedTokens=1 and burstCapacity=1
        // will result in a limit of 1 request per 1 second.
        // setting replenishRate=1, requestedTokens=60 and burstCapacity=60 will result in a limit of 1 request/min.
        filter.addArg(REPLENISH_RATE_KEY, redisRateLimiterConfiguration.getReplenishRate());
        filter.addArg(BURST_CAPACITY_KEY, redisRateLimiterConfiguration.getBurstCapacity());
        filter.addArg(REQUESTED_TOKENS_KEY, redisRateLimiterConfiguration.getRequestedTokens());
        discoveryLocatorProperties.getFilters().add(filter);
        log.info("Added filter[{}] for discovery services, filters: {}", RedisRateLimiter.class.getSimpleName(),
                 discoveryLocatorProperties.getFilters());
    }

    @Bean
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
