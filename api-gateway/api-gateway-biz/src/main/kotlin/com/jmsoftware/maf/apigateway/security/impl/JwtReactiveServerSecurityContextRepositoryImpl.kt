package com.jmsoftware.maf.apigateway.security.impl

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClientService
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal.Companion.createByUsername
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.property.JwtConfigurationProperties.Companion.TOKEN_PREFIX
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.NETWORK_AUTHENTICATION_REQUIRED
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.util.AntPathMatcher
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * # JwtReactiveServerSecurityContextRepositoryImpl
 *
 * Implementation of JWT reactive server security context repository
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:04 PM
 */
open class JwtReactiveServerSecurityContextRepositoryImpl(
    private val mafConfigurationProperties: MafConfigurationProperties,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val authCenterWebClientService: AuthCenterWebClientService
) : ServerSecurityContextRepository {
    private val antPathMatcher = AntPathMatcher()

    companion object {
        private val log = logger()
    }

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        log.error("Unsupported operation exception: Not supported yet.")
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val request = exchange.request
        // Ignore allowed URL
        for (ignoredUrl in mafConfigurationProperties.flattenIgnoredUrls()) {
            if (antPathMatcher.match(ignoredUrl, request.uri.path)) {
                return Mono.empty()
            }
        }
        val authorization = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (StrUtil.isBlank(authorization) || !StrUtil.startWith(authorization, TOKEN_PREFIX)) {
            log.warn("Pre-authentication failure! Cause: `${HttpHeaders.AUTHORIZATION}` in HTTP headers not found. Request URL: [${request.method}] ${request.uri}")
            return Mono.error(SecurityException("JWT Required", NETWORK_AUTHENTICATION_REQUIRED))
        }
        return authCenterWebClientService.parse(authorization!!)
            .map { parseJwtResponse: ParseJwtResponse ->
                log.info("parseJwtResponse: $parseJwtResponse")
                val userPrincipal = createByUsername(parseJwtResponse.username)
                userPrincipal.id = parseJwtResponse.id
                val authentication = UsernamePasswordAuthenticationToken(userPrincipal, null)
                log.warn("About to authenticate… Authentication is created. $authentication")
                authentication
            }.flatMap { authentication: UsernamePasswordAuthenticationToken ->
                authenticationManager.authenticate(authentication)
                    .map { authentication2: Authentication ->
                        SecurityContextImpl(
                            authentication2
                        )
                    }
            }
    }
}
