package com.jmsoftware.maf.gateway.universal.filter;

import com.jmsoftware.maf.gateway.universal.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <h1>RequestFilter</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/15/20 7:42 PM
 **/
@Slf4j
@Component
public class RequestFilter implements WebFilter {
    @Override
    @SuppressWarnings("NullableProblems")
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("{} (pre). Requester: {}, request URL: [{}] {}",
                 this.getClass().getSimpleName(),
                 RequestUtil.getRequestIpAndPort(exchange.getRequest()), exchange.getRequest().getMethod(),
                 exchange.getRequest().getURI());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> log.info("{} (post). Requester: {}, request URL: [{}] {}",
                                                 this.getClass().getSimpleName(),
                                                 RequestUtil.getRequestIpAndPort(exchange.getRequest()),
                                                 exchange.getRequest().getMethod(),
                                                 exchange.getRequest().getURI()))
        );
    }
}
