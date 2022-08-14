package com.jmsoftware.maf.apigateway.security.impl

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClientService
import com.jmsoftware.maf.common.constant.MafHttpHeader.X_ID
import com.jmsoftware.maf.common.constant.MafHttpHeader.X_USERNAME
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.util.AntPathMatcher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

/**
 * # RbacReactiveAuthorizationManagerImpl
 *
 * Implementation of RBAC (Role-based access control) reactive authorization manager
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:57 PM
 * @see [Role-based access control](https://en.wikipedia.org/wiki/Role-based_access_control)
 */
open class RbacReactiveAuthorizationManagerImpl(
    private val authCenterWebClientService: AuthCenterWebClientService
) : ReactiveAuthorizationManager<AuthorizationContext> {
    companion object {
        private val log = logger()
    }

    private val antPathMatcher = AntPathMatcher()

    /**
     * Retrieve roles flux.
     *
     * @param userPrincipalMono the user principal mono
     * @return the flux
     */
    private fun retrieveRoles(userPrincipalMono: Mono<UserPrincipal>): Flux<GetRoleListByUserIdSingleResponse> {
        // Get role list by user ID, and then convert to Flux<?>
        return userPrincipalMono
            .flatMap { userPrincipal: UserPrincipal -> authCenterWebClientService.getRoleListByUserId(userPrincipal.id!!) }
            .flatMapMany { Flux.fromIterable(it) }
            .switchIfEmpty(Flux.error(SecurityException("Roles not assigned!", UNAUTHORIZED)))
    }

    /**
     * Filter roles mono.
     *
     * @param roleFlux the role flux
     * @return the mono
     */
    private fun mapRole(roleFlux: Flux<GetRoleListByUserIdSingleResponse>): Mono<List<Long>> {
        return roleFlux
            .map(GetRoleListByUserIdSingleResponse::id)
            .collectList()
            .switchIfEmpty(roleFlux.map(GetRoleListByUserIdSingleResponse::id).collectList())
    }

    /**
     * Retrieve permissions mono.
     *
     * @param roleIdListMono the role id list mono
     * @return the mono
     */
    private fun retrievePermissions(roleIdListMono: Mono<List<Long>>): Mono<List<Permission>> {
        // Get permission list based on the Mono<List<Long>>
        // auth-center will respond /** for role "admin"
        return roleIdListMono.flatMap { roleIdList: List<Long> ->
            val payload = GetPermissionListByRoleIdListPayload()
            payload.roleIdList = roleIdList
            payload.permissionTypeList = listOf(PermissionType.BUTTON)
            authCenterWebClientService.getPermissionListByRoleIdList(payload)
        }
            .switchIfEmpty(Mono.error(SecurityException("Permission not found!", FORBIDDEN)))
    }

    override fun check(
        authentication: Mono<Authentication>,
        `object`: AuthorizationContext
    ): Mono<AuthorizationDecision> {
        val request = `object`.exchange.request
        val userPrincipalMono = authentication.map { auth: Authentication -> auth.principal as UserPrincipal }
        val roleFlux = retrieveRoles(userPrincipalMono)
        val roleIdListMono = mapRole(roleFlux)
        val permissionListMono = retrievePermissions(roleIdListMono)
        // Aggregate 2 Mono
        val zip = Mono.zip(permissionListMono, userPrincipalMono)
        return zip.map { mapper: Tuple2<List<Permission>, UserPrincipal> ->
            val userPrincipal = mapper.t2
            val anyMatched = mapper.t1.stream()
                .filter { permission: Permission -> StrUtil.isNotBlank(permission.url) }
                .filter { permission: Permission -> StrUtil.isNotBlank(permission.method) }
                .anyMatch { permission: Permission ->
                    checkRestfulAccess(permission, request)
                }
            if (anyMatched) {
                log.info("Authorization success! Resource [${request.method}] ${request.uri} is accessible for user(username: ${userPrincipal.username})")
                request.mutate()
                    .headers { httpHeaders: HttpHeaders ->
                        httpHeaders[X_ID.header] = userPrincipal.id.toString()
                        httpHeaders[X_USERNAME.header] = userPrincipal.username
                    }.build()
                AuthorizationDecision(true)
            } else {
                log.warn("Authorization failure! Resource [${request.method}] ${request.uri} is not accessible for user(username: ${userPrincipal.username})")
                AuthorizationDecision(false)
            }
        }
    }

    /**
     *
     * Check Restful access.
     *
     *  * Check if the URL is matched
     *  * Check if the HTTP method is matched
     *
     *
     * @param buttonPermission the button permission
     * @param request          the request
     * @return true: accessible; false: not accessible
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 1/13/2021 11:04 AM
     */
    private fun checkRestfulAccess(
        buttonPermission: Permission,
        request: ServerHttpRequest
    ): Boolean {
        val urlMatched = antPathMatcher.match(buttonPermission.url!!, request.uri.path)
        // "*" is for super user. Super user's permission is like URL: "/**", method: "*"
        val allMethods = StrUtil.equals(buttonPermission.method, "*")
        if (allMethods) {
            return urlMatched
        }
        val methodMatched = request.method!!.matches(buttonPermission.method!!)
        return urlMatched && methodMatched
    }
}
