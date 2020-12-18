package com.jmsoftware.maf.apiportal.universal.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.jmsoftware.maf.apiportal.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.apiportal.universal.configuration.CustomConfiguration;
import com.jmsoftware.maf.apiportal.universal.domain.UserPrincipal;
import com.jmsoftware.maf.apiportal.universal.service.RbacAuthorityService;
import com.jmsoftware.maf.common.constant.HttpStatus;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * <h1>RbacAuthorityServiceImpl</h1>
 * <p>Route Authority service implementation</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:25
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RbacAuthorityServiceImpl implements RbacAuthorityService {
    private final AuthCenterRemoteApi authCenterRemoteApi;
    private final RequestMappingHandlerMapping mapping;
    private final CustomConfiguration customConfiguration;
    private final JwtServiceImpl jwtServiceImpl;

    @Override
    @SneakyThrows
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        String username;
        try {
            username = jwtServiceImpl.getUsernameFromRequest(request);
        } catch (SecurityException e) {
            log.error("Exception occurred when getting username from request. Exception message: {}", e.getMessage());
            throw e;
        }
        // Super user has no restriction on any requests.
        if (customConfiguration.getSuperUser().equals(username)) {
            log.info("Superuser(username: {}) can access any resource.", username);
            return true;
        }
        this.checkRequest(request);
        val principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            log.error("Invalid user principal. {}", principal);
            return false;
        }
        val userPrincipal = (UserPrincipal) principal;
        val userId = userPrincipal.getId();
        val getPermissionListByUserIdPayload = new GetPermissionListByUserIdPayload();
        getPermissionListByUserIdPayload.setUserId(userId);
        val getPermissionListByUserIdResponse =
                authCenterRemoteApi.getPermissionListByUserId(getPermissionListByUserIdPayload);
        val permissionList = getPermissionListByUserIdResponse.getData().getPermissionList();
        // Filter button permission for frond-end
        val buttonPermissionList = permissionList.stream()
                // Sieve out page permissions
                .filter(permission -> ObjectUtil.equal(permission.getType(),
                                                       PermissionType.BUTTON.getType()))
                // Sieve out permission that has no URL
                .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
                // Sieve out permission that has no method
                .filter(permission -> StrUtil.isNotBlank(permission.getMethod()))
                .collect(Collectors.toList());
        for (val buttonPermission : buttonPermissionList) {
            // TODO: check is AntPathRequestMatcher supports RESTFul request
            val antPathMatcher = new AntPathRequestMatcher(buttonPermission.getUrl(), buttonPermission.getMethod());
            if (antPathMatcher.matches(request)) {
                log.info("Resource [{}] {} is accessible for user(username: {})", request.getMethod(),
                         request.getRequestURL(), username);
                return true;
            }
        }
        log.warn("Resource [{}] {} is not accessible for user(username: {})", request.getMethod(),
                 request.getRequestURL(), username);
        return false;
    }

    /**
     * Check request.
     *
     * @param request HTTP Request
     */
    @SneakyThrows
    private void checkRequest(HttpServletRequest request) {
        val currentMethod = request.getMethod();
        val urlMapping = allUrlMapping();
        for (String uri : urlMapping.keySet()) {
            // 通过 AntPathRequestMatcher 匹配 url
            // 可以通过 2 种方式创建 AntPathRequestMatcher
            // 1：new AntPathRequestMatcher(uri,method) 这种方式可以直接判断方法是否匹配，
            //  因为这里我们把 方法不匹配 自定义抛出，所以，我们使用第2种方式创建
            // 2：new AntPathRequestMatcher(uri) 这种方式不校验请求方法，只校验请求路径
            val antPathMatcher = new AntPathRequestMatcher(uri);
            if (antPathMatcher.matches(request)) {
                if (!urlMapping.get(uri).contains(currentMethod)) {
                    throw new SecurityException(HttpStatus.METHOD_NOT_ALLOWED);
                } else {
                    return;
                }
            }
        }
        throw new SecurityException(HttpStatus.NOT_FOUND);
    }

    /**
     * 获取 所有URL Mapping，返回格式为{"/test":["GET","POST"],"/sys":["GET","DELETE"]}
     *
     * @return {@link ArrayListMultimap} 格式的 URL Mapping
     */
    private Multimap<String, String> allUrlMapping() {
        Multimap<String, String> urlMapping = LinkedListMultimap.create();
        // 获取url与类和方法的对应信息
        val handlerMethods = mapping.getHandlerMethods();
        handlerMethods.forEach((key, value) -> {
            // 获取当前 key 下的获取所有URL
            val url = key.getPatternsCondition().getPatterns();
            val method = key.getMethodsCondition();
            // 为每个URL添加所有的请求方法
            url.forEach(item -> urlMapping.putAll(item,
                                                  method.getMethods().stream().map(Enum::toString).collect(Collectors.toList())));
        });
        return urlMapping;
    }
}
