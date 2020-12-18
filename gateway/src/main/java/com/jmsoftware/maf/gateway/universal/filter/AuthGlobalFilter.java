package com.jmsoftware.maf.gateway.universal.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: AuthGlobalFilter, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/18/2020 2:52 PM
 * @see
 * <a href='https://cloud.spring.io/spring-cloud-gateway/multi/multi__global_filters.html#_global_filters'>Global Filters</a>
 **/
@Component
@Order(Integer.MIN_VALUE)
public class AuthGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }
}
