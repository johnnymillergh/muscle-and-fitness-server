package com.jmsoftware.maf.apigateway.security;

import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Description: ReactiveUserDetailsServiceImpl, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/24/2020 2:12 PM
 **/
@Slf4j
@RequiredArgsConstructor
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {
    private final JwtService jwtService;
    @Lazy
    @Resource
    private AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.warn("findByUsername: {}", username);
        return null;
    }
}
