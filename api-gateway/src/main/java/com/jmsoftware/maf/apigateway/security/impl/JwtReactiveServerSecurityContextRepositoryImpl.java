package com.jmsoftware.maf.apigateway.security.impl;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.reactivespringcloudstarter.property.JwtConfigurationProperties;
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: JwtReactiveServerSecurityContextRepositoryImpl
 * <p>
 * Implementation of JWT reactive server security context repository
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 9:58 AM
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtReactiveServerSecurityContextRepositoryImpl implements ServerSecurityContextRepository {
    private final MafConfigurationProperties mafConfigurationProperties;
    private final ReactiveAuthenticationManager authenticationManager;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        log.error("Unsupported operation exception: Not supported yet.");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        val request = exchange.getRequest();
        // Ignore allowed URL
        for (val ignoredUrl : this.mafConfigurationProperties.flattenIgnoredUrls()) {
            if (this.antPathMatcher.match(ignoredUrl, request.getURI().getPath())) {
                return Mono.empty();
            }
        }
        val authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(authorization) || !StrUtil.startWith(authorization, JwtConfigurationProperties.TOKEN_PREFIX)) {
            log.warn("Pre-authentication failure! Cause: `{}` in HTTP headers not found. Request URL: [{}] {}",
                     HttpHeaders.AUTHORIZATION, request.getMethod(), request.getURI());
            return Mono.error(new SecurityException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "JWT Required"));
        }
        return this.authCenterRemoteApi.parse(authorization)
                .map(parseJwtResponse -> {
                    log.info("parseJwtResponse: {}", parseJwtResponse);
                    val userPrincipal = UserPrincipal.createByUsername(parseJwtResponse.getUsername());
                    userPrincipal.setId(parseJwtResponse.getId());
                    val authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null);
                    log.warn("About to authenticate… Authentication is created. {}", authentication);
                    return authentication;
                }).flatMap(authentication -> this.authenticationManager.authenticate(authentication)
                        .map(SecurityContextImpl::new));
    }
}
