package com.jmsoftware.maf.gateway.universal.configuration;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: ReactiveServerSecurityContextRepository, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/18/2020 3:38 PM
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveServerSecurityContextRepository implements ServerSecurityContextRepository {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(authorization) || !authorization.startsWith(TOKEN_PREFIX)) {
            log.warn("Authentication failed! Cause: `{}` in HTTP headers not found. Request URL: [{}] {}",
                     HttpHeaders.AUTHORIZATION, request.getMethod(), request.getURI());
            return Mono.empty();
        }
        String jwt = authorization.replace(TOKEN_PREFIX, "");
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, jwt);
        return this.authenticationManager.authenticate(authentication).map(SecurityContextImpl::new);
    }
}
