package com.jmsoftware.maf.apigateway.remoteapi;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <h1>AuthCenterRemoteApi</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/10/20 4:50 PM
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AuthCenterRemoteApi {
    private static final String SERVICE_NAME = "auth-center";
    private final WebClient webClient;

    /**
     * Gets user by login token.
     *
     * @param loginToken the login token, e.q. username, email or phone number
     * @return the user by login token
     */
    public Mono<GetUserByLoginTokenResponse> getUserByLoginToken(@PathVariable String loginToken) {
        return this.webClient
                .get()
                .uri(String.format("http://%s/user-remote-api/users/{loginToken}", SERVICE_NAME), loginToken)
                .retrieve()
                .bodyToMono(ResponseBodyBean.class)
                .map(ResponseBodyBean::getData)
                .map(data -> JSONUtil.toBean(JSONUtil.parseObj(data), GetUserByLoginTokenResponse.class));
    }

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    public Mono<List<GetRoleListByUserIdSingleResponse>> getRoleListByUserId(@NotNull Long userId) {
        return this.webClient
                .get()
                .uri(String.format("http://%s/role-remote-api/roles/{userId}", SERVICE_NAME), userId)
                .retrieve()
                .bodyToMono(ResponseBodyBean.class)
                .map(ResponseBodyBean::getData)
                .map(data -> JSONUtil.toList(JSONUtil.parseObj(data).getJSONArray("roleList"),
                                             GetRoleListByUserIdSingleResponse.class));
    }

    /**
     * Get permission list by role id list
     *
     * @param payload the payload
     * @return the response body bean
     */
    public Mono<List<GetPermissionListByRoleIdListResponse.Permission>> getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload) {
        return this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .host("auth-center")
                        .path("/permission-remote-api/permissions")
                        .queryParam("roleIdList", StrUtil.join(",", payload.getRoleIdList()))
                        .queryParam("permissionTypeList", StrUtil.join(",", payload.getPermissionTypeList()))
                        .build())
                .retrieve()
                .bodyToMono(ResponseBodyBean.class)
                .map(ResponseBodyBean::getData)
                .map(data -> JSONUtil.toList(JSONUtil.parseObj(data).getJSONArray("permissionList"),
                                             GetPermissionListByRoleIdListResponse.Permission.class));
    }

    /**
     * Parse JWT.
     *
     * @param authorization the authorization
     * @return the mono
     */
    @GetMapping("/jwt-remote-api/parse")
    public Mono<ParseJwtResponse> parse(@NotBlank String authorization) {
        return this.webClient
                .get()
                .uri("http://auth-center/jwt-remote-api/parse")
                .headers(httpHeaders -> httpHeaders.set(HttpHeaders.AUTHORIZATION, authorization))
                .retrieve()
                .bodyToMono(ResponseBodyBean.class).map(ResponseBodyBean::getData)
                .map(data -> JSONUtil.toBean(JSONUtil.parseObj(data), ParseJwtResponse.class));
    }
}
