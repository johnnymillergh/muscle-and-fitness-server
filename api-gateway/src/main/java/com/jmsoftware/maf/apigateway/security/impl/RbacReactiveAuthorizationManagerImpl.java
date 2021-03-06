package com.jmsoftware.maf.apigateway.security.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.common.constant.MafHttpHeader;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: RbacReactiveAuthorizationManagerImpl
 * <p>
 * Implementation of RBAC (Role-based access control) reactive authorization manager
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 9:54 AM
 * @see <a href='https://en.wikipedia.org/wiki/Role-based_access_control'>Role-based access control</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacReactiveAuthorizationManagerImpl implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final AuthCenterRemoteApi authCenterRemoteApi;

    /**
     * Retrieve roles flux.
     *
     * @param userPrincipalMono the user principal mono
     * @return the flux
     */
    private Flux<GetRoleListByUserIdSingleResponse> retrieveRoles(Mono<UserPrincipal> userPrincipalMono) {
        // Get role list by user ID, and then convert to Flux<?>
        return userPrincipalMono
                .flatMap(userPrincipal -> this.authCenterRemoteApi.getRoleListByUserId(userPrincipal.getId()))
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.error(new SecurityException(HttpStatus.UNAUTHORIZED, "Roles not assigned!")));
    }

    /**
     * Filter roles mono.
     *
     * @param roleFlux the role flux
     * @return the mono
     */
    private Mono<List<Long>> mapRole(Flux<GetRoleListByUserIdSingleResponse> roleFlux) {
        return roleFlux
                .map(GetRoleListByUserIdSingleResponse::getId)
                .collectList()
                .switchIfEmpty(roleFlux.map(GetRoleListByUserIdSingleResponse::getId).collectList());
    }

    /**
     * Retrieve permissions mono.
     *
     * @param roleIdListMono the role id list mono
     * @return the mono
     */
    private Mono<List<GetPermissionListByRoleIdListResponse.Permission>> retrievePermissions(Mono<List<Long>> roleIdListMono) {
        // Get permission list based on the Mono<List<Long>>
        // auth-center will respond /** for role "admin"
        return roleIdListMono.flatMap(
                roleIdList -> {
                    val payload = new GetPermissionListByRoleIdListPayload();
                    payload.setRoleIdList(roleIdList);
                    payload.setPermissionTypeList(Lists.newArrayList(PermissionType.BUTTON));
                    return this.authCenterRemoteApi.getPermissionListByRoleIdList(payload);
                })
                .switchIfEmpty(Mono.error(new SecurityException(HttpStatus.FORBIDDEN, "Permission not found!")));
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        val request = object.getExchange().getRequest();
        val userPrincipalMono = authentication.map(auth -> (UserPrincipal) auth.getPrincipal());
        val roleFlux = this.retrieveRoles(userPrincipalMono);
        val roleIdListMono = this.mapRole(roleFlux);
        val permissionListMono = this.retrievePermissions(roleIdListMono);
        // Aggregate 2 Mono
        val zip = Mono.zip(permissionListMono, userPrincipalMono);
        return zip.map(mapper -> {
            val permissionList = mapper.getT1();
            val buttonPermissionList = permissionList.stream()
                    .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
                    .filter(permission -> StrUtil.isNotBlank(permission.getMethod()))
                    .collect(Collectors.toList());
            val userPrincipal = mapper.getT2();
            for (var buttonPermission : buttonPermissionList) {
                if (this.checkRestfulAccess(buttonPermission, request)) {
                    log.info("Authorization success! Resource [{}] {} is accessible for user(username: {})",
                             request.getMethod(), request.getURI(), userPrincipal.getUsername());
                    request
                            .mutate()
                            .headers(httpHeaders -> httpHeaders.set(MafHttpHeader.X_USERNAME.getHeader(),
                                                                    userPrincipal.getUsername()))
                            .build();
                    return new AuthorizationDecision(true);
                }
            }
            log.warn("Authorization failure! Resource [{}] {} is not accessible for user(username: {})",
                     request.getMethod(), request.getURI(), userPrincipal.getUsername());
            return new AuthorizationDecision(false);
        });
    }

    /**
     * <p>Check Restful access.</p>
     * <ul>
     * <li>Check if the URL is matched</li>
     * <li>Check if the HTTP method is matched</li>
     * </ul>
     *
     * @param buttonPermission the button permission
     * @param request          the request
     * @return true: accessible; false: not accessible
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 1/13/2021 11:04 AM
     */
    private boolean checkRestfulAccess(GetPermissionListByRoleIdListResponse.Permission buttonPermission,
                                       ServerHttpRequest request) {
        val urlMatched = this.antPathMatcher.match(buttonPermission.getUrl(), request.getURI().getPath());
        // "*" is for super user. Super user's permission is like URL: "/**", method: "*"
        val allMethods = StrUtil.equals(buttonPermission.getMethod(), "*");
        if (allMethods) {
            return urlMatched;
        }
        val methodMatched = Objects.requireNonNull(request.getMethod()).matches(buttonPermission.getMethod());
        return urlMatched && methodMatched;
    }
}
