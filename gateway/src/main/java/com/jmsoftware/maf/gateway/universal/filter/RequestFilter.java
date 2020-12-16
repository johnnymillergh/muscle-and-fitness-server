package com.jmsoftware.maf.gateway.universal.filter;

import com.jmsoftware.maf.gateway.universal.configuration.ProjectProperty;
import com.jmsoftware.maf.gateway.universal.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <h1>RequestFilter</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/15/20 7:42 PM
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestFilter implements GlobalFilter {
    private final ProjectProperty projectProperty;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("{} intercepted requesters access. Requester: {}, resource: [{}] {}",
                 projectProperty.getProjectArtifactId().toUpperCase(),
                 RequestUtil.getRequestIpAndPort(exchange.getRequest()), exchange.getRequest().getMethod(),
                 exchange.getRequest().getURI());
        return chain.filter(exchange);
    }
}
