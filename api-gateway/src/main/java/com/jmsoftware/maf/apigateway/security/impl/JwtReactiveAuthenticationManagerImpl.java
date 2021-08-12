package com.jmsoftware.maf.apigateway.security.impl;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.exception.BizException;
import com.jmsoftware.maf.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import reactor.core.publisher.Mono;

/**
 * Description: JwtReactiveAuthenticationManagerImpl
 * <p>
 * Implementation of JWT reactive authentication manager
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 9:57 AM
 **/
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManagerImpl implements ReactiveAuthenticationManager {
    private final static Logger log = LoggerFactory.getLogger(JwtReactiveAuthenticationManagerImpl.class);
    private final AuthCenterRemoteApi authCenterRemoteApi;

    private final UserDetailsChecker preAuthenticationChecks = user -> {
        if (!user.isAccountNonLocked()) {
            log.error("User account is locked");
            throw new LockedException("User account is locked");
        }

        if (!user.isEnabled()) {
            log.error("User account is disabled");
            throw new DisabledException("User is disabled");
        }

        if (!user.isAccountNonExpired()) {
            log.error("User account is expired");
            throw new AccountExpiredException("User account has expired");
        }
    };

    private final UserDetailsChecker postAuthenticationChecks = user -> {
        if (!user.isCredentialsNonExpired()) {
            log.error("User account credentials have expired");
            throw new CredentialsExpiredException("User credentials have expired");
        }
    };

    private Mono<UserDetails> retrieveUser(String username) {
        if (StrUtil.isBlank(username)) {
            log.warn("Authentication failure! Cause: the username mustn't be blank");
            return Mono.error(
                    new SecurityException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "Username mustn't be blank"));
        }
        return this.authCenterRemoteApi.getUserByLoginToken(username)
                .switchIfEmpty(Mono.error(new BizException("Authentication failure! Cause: User not found")))
                .map(data -> {
                    log.info("Authentication success! Found {}", data);
                    return UserPrincipal.create(data, null, null);
                });
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return this.retrieveUser(authentication.getName())
                .doOnNext(this.preAuthenticationChecks::check)
                .doOnNext(this.postAuthenticationChecks::check)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null));
    }
}
