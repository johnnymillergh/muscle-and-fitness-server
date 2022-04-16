package com.jmsoftware.maf.apigateway.security.impl

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * # ServerAuthenticationEntryPointImpl
 *
 * Description: ServerAuthenticationEntryPointImpl, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:57 PM
 */
open class ServerAuthenticationEntryPointImpl(
    private val responseUtil: ResponseUtil
) : ServerAuthenticationEntryPoint {
    companion object {
        private val log = logger()
    }

    override fun commence(exchange: ServerWebExchange, e: AuthenticationException): Mono<Void> {
        log.error("Exception occurred when authenticating! Exception message: ${e.message}. Request URL: [${exchange.request.method}] ${exchange.request.uri}")
        return responseUtil.renderJson(exchange, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, e.message, null)
    }
}
