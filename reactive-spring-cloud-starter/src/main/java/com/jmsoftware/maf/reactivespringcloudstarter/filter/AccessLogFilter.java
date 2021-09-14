package com.jmsoftware.maf.reactivespringcloudstarter.filter;

import com.jmsoftware.maf.reactivespringcloudstarter.configuration.MafConfiguration;
import com.jmsoftware.maf.reactivespringcloudstarter.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <h1>AccessLogFilter</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/24/2020 10:56 AM
 **/
@Slf4j
@RequiredArgsConstructor
public class AccessLogFilter implements WebFilter, Ordered {
    private final MafConfiguration mafConfiguration;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    @SuppressWarnings("NullableProblems")
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        for (String ignoredUrl : this.mafConfiguration.flattenIgnoredUrls()) {
            if (this.antPathMatcher.match(ignoredUrl, request.getURI().getPath())) {
                return chain.filter(exchange);
            }
        }
        // Only record non-ignored request log
        log.info("{} (pre). Requester: {}, request URL: [{}] {}",
                 this.getClass().getSimpleName(), RequestUtil.getRequesterIpAndPort(request), request.getMethod(),
                 request.getURI());
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> log.info("{} (post). Requester: {}, request URL: [{}] {}",
                                                 this.getClass().getSimpleName(),
                                                 RequestUtil.getRequesterIpAndPort(request), request.getMethod(),
                                                 request.getURI()))
        );
    }

    @Override
    public int getOrder() {
        return -500;
    }
}
