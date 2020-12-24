package com.jmsoftware.maf.apigateway.security;

import com.jmsoftware.maf.reactivespringbootstarter.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: CustomServerAccessDeniedHandler, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/21/2020 10:12 AM
 **/
@Slf4j
@Configuration
public class GatewayServerAccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("Access denied! Exception message: {}. Request URL: [{}] {}", denied.getMessage(),
                  exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        return ResponseUtil.renderJson(exchange, HttpStatus.FORBIDDEN, null);
    }
}
