package com.jmsoftware.maf.gateway.universal.configuration;

import com.jmsoftware.maf.gateway.remoteapi.AuthCenterRemoteApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Description: ReactiveAuthorizationManagerImpl, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/21/2020 12:38 PM
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Lazy
    @Resource
    private AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        return authentication.map(auth -> {
            ServerHttpRequest request = object.getExchange().getRequest();
            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            log.info("Checking authorization for user: {}, resource: [{}] {}", userPrincipal.getUsername(),
                     request.getMethod(), request.getURI());
            return new AuthorizationDecision(true);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }
}
