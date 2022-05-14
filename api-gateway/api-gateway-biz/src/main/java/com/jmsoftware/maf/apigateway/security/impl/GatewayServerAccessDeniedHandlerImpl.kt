package com.jmsoftware.maf.apigateway.security.impl

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * # GatewayServerAccessDeniedHandlerImpl
 *
 * Implementation os gateway server access denied handler
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:03 PM
 */
open class GatewayServerAccessDeniedHandlerImpl(
    private val responseUtil: ResponseUtil
) : ServerAccessDeniedHandler {
    companion object {
        private val log = logger()
    }

    override fun handle(exchange: ServerWebExchange, denied: AccessDeniedException): Mono<Void> {
        log.error("Access denied! Exception message: ${denied.message}. Request URL: [${exchange.request.method}] ${exchange.request.uri}")
        return responseUtil.renderJson(exchange, HttpStatus.FORBIDDEN, denied.message, null)
    }
}
