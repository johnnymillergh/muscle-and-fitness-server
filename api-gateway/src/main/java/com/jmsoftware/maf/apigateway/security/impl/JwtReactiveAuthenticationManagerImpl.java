package com.jmsoftware.maf.apigateway.security.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import com.jmsoftware.maf.common.exception.BusinessException;
import com.jmsoftware.maf.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Description: JwtReactiveAuthenticationManagerImpl
 * <p>
 * Implementation of JWT reactive authentication manager
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 9:57 AM
 **/
@Slf4j
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManagerImpl implements ReactiveAuthenticationManager {
    @Resource
    private WebClient.Builder webClientBuilder;

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
        val response = webClientBuilder
                .build()
                .get()
                .uri("http://auth-center/user-remote-api/users/{loginToken}", username)
                .retrieve()
                .bodyToMono(ResponseBodyBean.class);
        return response.map(ResponseBodyBean::getData)
                .switchIfEmpty(Mono.error(new BusinessException("Authentication failure! Cause: User not found")))
                .map(data -> {
                    log.info("Authentication success! Found {}", data);
                    val getUserByLoginTokenResponse = JSONUtil.toBean(JSONUtil.parseObj(data),
                                                                      GetUserByLoginTokenResponse.class);
                    return UserPrincipal.create(getUserByLoginTokenResponse, null, null);
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
