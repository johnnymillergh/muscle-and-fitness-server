package com.jmsoftware.maf.apigateway.security.impl;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.apigateway.security.configuration.JwtConfiguration;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtPayload;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Lazy;
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

import javax.annotation.Resource;

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
    private final MafConfiguration mafConfiguration;
    private final ReactiveAuthenticationManager authenticationManager;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Lazy
    @Resource
    private AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        log.error("Unsupported operation exception: Not supported yet.");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        val request = exchange.getRequest();
        // Ignore allowed URL
        for (var ignoredUrl : mafConfiguration.flattenIgnoredUrls()) {
            if (antPathMatcher.match(ignoredUrl, request.getURI().getPath())) {
                return Mono.empty();
            }
        }
        val authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(authorization) || !authorization.startsWith(JwtConfiguration.TOKEN_PREFIX)) {
            log.warn("Pre-authentication failure! Cause: `{}` in HTTP headers not found. Request URL: [{}] {}",
                     HttpHeaders.AUTHORIZATION, request.getMethod(), request.getURI());
            return Mono.error(new SecurityException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "JWT Required"));
        }
        val jwt = authorization.replace(JwtConfiguration.TOKEN_PREFIX, "");
        val parseJwtPayload = new ParseJwtPayload();
        parseJwtPayload.setJwt(jwt);
        Mono<ParseJwtResponse> parseJwtResponseMono = authCenterRemoteApi
                .parse(parseJwtPayload)
                .map(ResponseBodyBean::getData)
                .switchIfEmpty(Mono.error(
                        new SecurityException(HttpStatus.INTERNAL_SERVER_ERROR, "Got empty when parsing JWT")));
        return parseJwtResponseMono.map(parseJwtResponse -> {
            String username = parseJwtResponse.getUsername();
            val userPrincipal = UserPrincipal.createByUsername(username);
            log.info("User principal is created. {}", userPrincipal);
            val authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null);
            log.warn("About to authenticate… Authentication is created. {}", authentication);
            return authentication;
        }).flatMap(authentication -> this.authenticationManager
                .authenticate(authentication).map(SecurityContextImpl::new));
    }
}
