package com.jmsoftware.maf.apigateway.security.impl;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
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
    private final MafConfiguration mafConfiguration;
    @Lazy
    @Resource
    private AuthCenterRemoteApi authCenterRemoteApi;

    /**
     * Retrieve roles flux.
     *
     * @param userPrincipalMono the user principal mono
     * @return the flux
     */
    private Flux<GetRoleListByUserIdResponse.Role> retrieveRoles(Mono<UserPrincipal> userPrincipalMono) {
        // Get role list by user ID, and then convert to Flux<?>
        return userPrincipalMono
                .flatMap(userPrincipal -> authCenterRemoteApi.getRoleListByUserId(userPrincipal.getId())
                        .map(ResponseBodyBean::getData))
                .map(GetRoleListByUserIdResponse::getRoleList)
                .flatMapMany(Flux::fromIterable)
                .switchIfEmpty(Flux.error(new SecurityException(HttpStatus.UNAUTHORIZED, "Roles not assigned!")));
    }

    /**
     * Filter roles mono.
     *
     * @param roleFlux the role flux
     * @return the mono
     */
    private Mono<List<Long>> filterRoles(Flux<GetRoleListByUserIdResponse.Role> roleFlux) {
        // Filter "admin" role and then, map Flux<?> to Mono<List<Long>>
        return roleFlux
                .filter(role -> StrUtil.equals(mafConfiguration.getSuperUserRole(), role.getName()))
                .map(GetRoleListByUserIdResponse.Role::getId)
                .collectList()
                .switchIfEmpty(roleFlux.map(GetRoleListByUserIdResponse.Role::getId).collectList());
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
                    GetPermissionListByRoleIdListPayload payload = new GetPermissionListByRoleIdListPayload();
                    payload.setRoleIdList(roleIdList);
                    return authCenterRemoteApi.getPermissionListByRoleIdList(payload.getRoleIdList()).map(
                            ResponseBodyBean::getData);
                }).map(GetPermissionListByRoleIdListResponse::getPermissionList)
                .switchIfEmpty(Mono.error(new SecurityException(HttpStatus.FORBIDDEN, "Permission not found!")));
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        val request = object.getExchange().getRequest();
        val userPrincipalMono = authentication.map(auth -> (UserPrincipal) auth.getPrincipal());
        Flux<GetRoleListByUserIdResponse.Role> roleFlux = this.retrieveRoles(userPrincipalMono);
        Mono<List<Long>> roleIdListMono = this.filterRoles(roleFlux);
        Mono<List<GetPermissionListByRoleIdListResponse.Permission>> permissionListMono = this.retrievePermissions(
                roleIdListMono);
        // Aggregate 2 Mono
        val zip = Mono.zip(permissionListMono, userPrincipalMono);
        return zip.map(mapper -> {
            val permissionList = mapper.getT1();
            val buttonPermissionList = permissionList.stream()
                    .filter(permission -> PermissionType.BUTTON.getType().equals(permission.getType()))
                    .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
                    .filter(permission -> StrUtil.isNotBlank(permission.getMethod()))
                    .collect(Collectors.toList());
            val path = request.getURI().getPath();
            val userPrincipal = mapper.getT2();
            for (val buttonPermission : buttonPermissionList) {
                // FIXME: currently, the `method` in permission is useless
                if (antPathMatcher.match(buttonPermission.getUrl(), path)) {
                    log.info("Authorization success! Resource [{}] {} is accessible for user(username: {})",
                             request.getMethod(), request.getURI(), userPrincipal.getUsername());
                    return new AuthorizationDecision(true);
                }
            }
            log.warn("Authorization failure! Resource [{}] {} is inaccessible for user(username: {})",
                     request.getMethod(), request.getURI(), userPrincipal.getUsername());
            return new AuthorizationDecision(false);
        });
    }
}
