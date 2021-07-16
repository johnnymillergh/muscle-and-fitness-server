package com.jmsoftware.maf.apigateway.security.impl;

import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Description: GatewayServerAccessDeniedHandlerImpl
 * <p>
 * Implementation os gateway server access denied handler
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 9:56 AM
 **/
@Slf4j
public class GatewayServerAccessDeniedHandlerImpl implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.error("Access denied! Exception message: {}. Request URL: [{}] {}", denied.getMessage(),
                  exchange.getRequest().getMethod(), exchange.getRequest().getURI());
        return ResponseUtil.renderJson(exchange, HttpStatus.FORBIDDEN, denied.getMessage(), null);
    }
}
