package com.jmsoftware.maf.gateway.universal.configuration;

import com.jmsoftware.maf.gateway.universal.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: ServerAuthenticationEntryPointImpl, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/21/2020 9:48 AM
 **/
@Slf4j
@Configuration
public class ServerAuthenticationEntryPointImpl implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
        log.error("Exception occurred when authenticating! Exception message: {}. Request URL: [{}] {}", e.getMessage(),
                  serverWebExchange.getRequest().getMethod(), serverWebExchange.getRequest().getURI());
        return ResponseUtil.renderJson(serverWebExchange, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, null);
    }
}