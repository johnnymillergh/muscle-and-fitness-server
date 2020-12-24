package com.jmsoftware.maf.reactivespringbootstarter.filter;

import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafConfiguration;
import com.jmsoftware.maf.reactivespringbootstarter.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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
@RequiredArgsConstructor
public class AccessLogFilter implements WebFilter {
    private final MafConfiguration mafConfiguration;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    @SuppressWarnings("NullableProblems")
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        for (String ignoredUrl : mafConfiguration.flattenIgnoredUrls()) {
            if (antPathMatcher.match(ignoredUrl, request.getURI().getPath())) {
                return chain.filter(exchange);
            }
        }
        // Only record non-ignored request log
        log.info("{} (pre). Requester: {}, request URL: [{}] {}",
                 this.getClass().getSimpleName(), RequestUtil.getRequestIpAndPort(request), request.getMethod(),
                 request.getURI());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> log.info("{} (post). Requester: {}, request URL: [{}] {}",
                                                 this.getClass().getSimpleName(),
                                                 RequestUtil.getRequestIpAndPort(request), request.getMethod(),
                                                 request.getURI()))
        );
    }
}