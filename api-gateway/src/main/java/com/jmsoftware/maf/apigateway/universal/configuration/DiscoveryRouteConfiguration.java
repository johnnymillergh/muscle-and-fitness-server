package com.jmsoftware.maf.apigateway.universal.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Configuration;

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
    private final DiscoveryLocatorProperties discoveryLocatorProperties;

    @PostConstruct
    void postConstruct() {
        val filter = new FilterDefinition();
        filter.setName("RequestRateLimiter");
        // TODO: arguments should be passed from configuration dynamically
        filter.addArg("redis-rate-limiter.replenishRate", "1");
        filter.addArg("redis-rate-limiter.burstCapacity", "1");
        filter.addArg("redis-rate-limiter.requestedTokens", "1");
        discoveryLocatorProperties.getFilters().add(filter);
        log.info("Added filter[{}] for discovery services, filters: {}", RedisRateLimiter.class.getSimpleName(),
                 discoveryLocatorProperties.getFilters());
    }
}
