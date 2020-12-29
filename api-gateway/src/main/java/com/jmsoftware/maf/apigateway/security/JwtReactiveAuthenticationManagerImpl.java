package com.jmsoftware.maf.apigateway.security;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    @Lazy
    @Resource
    private AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        val username = authentication.getName();
        if (StrUtil.isBlank(username)) {
            log.warn("Authentication failure! Cause: the username mustn't be blank");
            return Mono.empty();
        }
        val response = authCenterRemoteApi.getUserByLoginToken(username);
        val responseMono = response.map(ResponseBodyBean::getData)
                .switchIfEmpty(Mono.error(new BusinessException("Authentication failure! Cause: User not found")));
        return responseMono.map(getUserByLoginTokenResponse -> {
            log.info("Authentication success! Found {}", getUserByLoginTokenResponse);
            UserPrincipal userPrincipal = UserPrincipal.create(getUserByLoginTokenResponse, null, null);
            return new UsernamePasswordAuthenticationToken(userPrincipal, null);
        });
    }
}
