package com.jmsoftware.maf.gateway.universal.configuration;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.exception.BusinessException;
import com.jmsoftware.maf.gateway.remoteapi.AuthCenterRemoteApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: ReactiveAuthorizationManagerImpl, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/21/2020 12:38 PM
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Lazy
    @Resource
    private AuthCenterRemoteApi authCenterRemoteApi;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {
        val request = object.getExchange().getRequest();
        Mono<Mono<GetPermissionListByUserIdResponse>> map = authentication.map(auth -> {
            val userPrincipal = (UserPrincipal) auth.getPrincipal();
            log.info("Checking authorization for user: {}, resource: [{}] {}", userPrincipal.getUsername(),
                     request.getMethod(), request.getURI());
            val responseBodyBeanMono =
                    authCenterRemoteApi.getPermissionListByUserId(userPrincipal.getId());
            return responseBodyBeanMono.map(ResponseBodyBean::getData)
                    .switchIfEmpty(Mono.error(new BusinessException("Permission mustn't be null!")));
        });
        return map.map(response -> {
            val permissionList = Objects.requireNonNull(response.block()).getPermissionList();
            val buttonPermissionList = permissionList.stream()
                    .filter(permission -> PermissionType.BUTTON.getType().equals(permission.getType()))
                    .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
                    .filter(permission -> StrUtil.isNotBlank(permission.getMethod()))
                    .collect(Collectors.toList());
            String path = request.getURI().getPath();
            for (val buttonPermission : buttonPermissionList) {
                if (antPathMatcher.match(buttonPermission.getUrl(), path)) {
                    val userPrincipal = (UserPrincipal) Objects.requireNonNull(authentication.block()).getPrincipal();
                    log.info("Resource [{}] {} is accessible for user(username: {})", request.getMethod(),
                             request.getURI(), userPrincipal.getUsername());
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        });
    }
}
