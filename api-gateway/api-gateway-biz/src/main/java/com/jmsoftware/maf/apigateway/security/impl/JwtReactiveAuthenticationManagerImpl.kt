package com.jmsoftware.maf.apigateway.security.impl

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.apigateway.remote.AuthCenterRemoteApi
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import com.jmsoftware.maf.common.exception.InternalServerException
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsChecker
import reactor.core.publisher.Mono

/**
 * # JwtReactiveAuthenticationManagerImpl
 *
 * Implementation of JWT reactive authentication manager
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:03 PM
 */
open class JwtReactiveAuthenticationManagerImpl(
    private val authCenterRemoteApi: AuthCenterRemoteApi
) : ReactiveAuthenticationManager {
    companion object {
        private val log = logger()
    }

    private val preAuthenticationChecks = UserDetailsChecker { user: UserDetails ->
        if (!user.isAccountNonLocked) {
            log.error("User account is locked")
            throw LockedException("User account is locked")
        }
        if (!user.isEnabled) {
            log.error("User account is disabled")
            throw DisabledException("User is disabled")
        }
        if (!user.isAccountNonExpired) {
            log.error("User account is expired")
            throw AccountExpiredException("User account has expired")
        }
    }
    private val postAuthenticationChecks = UserDetailsChecker { user: UserDetails ->
        if (!user.isCredentialsNonExpired) {
            log.error("User account credentials have expired")
            throw CredentialsExpiredException("User credentials have expired")
        }
    }

    private fun retrieveUser(username: String): Mono<UserDetails> {
        if (StrUtil.isBlank(username)) {
            log.warn("Authentication failure! Cause: the username mustn't be blank")
            return Mono.error(
                SecurityException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "Username mustn't be blank")
            )
        }
        return authCenterRemoteApi.getUserByLoginToken(username)
            .switchIfEmpty(Mono.error(InternalServerException("Authentication failure! Cause: User not found")))
            .map { data: GetUserByLoginTokenResponse ->
                log.info("Authentication success! Found {}", data)
                UserPrincipal.create(data)
            }
    }

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return retrieveUser(authentication.name)
            .doOnNext { toCheck: UserDetails -> preAuthenticationChecks.check(toCheck) }
            .doOnNext { toCheck: UserDetails -> postAuthenticationChecks.check(toCheck) }
            .map { userDetails: UserDetails -> UsernamePasswordAuthenticationToken(userDetails, null) }
    }
}
