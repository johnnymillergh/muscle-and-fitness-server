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
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Description: AuthenticationManager, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/18/2020 3:40 PM
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtService jwtService;
    @Lazy
    @Resource
    private AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        val jwt = authentication.getCredentials().toString();
        String username;
        try {
            username = jwtService.getUsernameFromJwt(jwt);
        } catch (Exception e) {
            log.error("Authentication failure! Cause: {}", e.getMessage());
            return Mono.empty();
        }
        if (StrUtil.isBlank(username)) {
            log.warn("Authentication failure! Cause: the username mustn't be blank");
            return Mono.empty();
        }
        val response = authCenterRemoteApi.getUserByLoginToken(username);
        val responseMono = response.map(ResponseBodyBean::getData)
                .switchIfEmpty(Mono.error(new BusinessException("Authentication failure! Cause: User not found")));
        return responseMono.map(getUserByLoginTokenResponse -> {
            log.info("Authentication success! Found username: {}", getUserByLoginTokenResponse.getUsername());
            UserPrincipal userPrincipal = UserPrincipal.create(getUserByLoginTokenResponse, null, null);
            return new UsernamePasswordAuthenticationToken(userPrincipal, null);
        });
    }
}
