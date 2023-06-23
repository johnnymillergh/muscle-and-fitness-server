package com.jmsoftware.maf.apigateway.remote

import cn.hutool.json.JSONUtil
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono

/**
 * # AuthCenterWebClient
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 9:36 PM
 */
@Service
@Validated
@Suppress("HttpUrlsUsage")
class AuthCenterWebClient(
    private val webClient: WebClient
) {
    companion object {
        private const val SERVICE_NAME = "auth-center"
    }

    /**
     * Gets user by login token.
     *
     * @param loginToken the login token, e.q. username, email or phone number
     * @return the user by login token
     */
    fun getUserByLoginToken(loginToken: @NotBlank String): Mono<GetUserByLoginTokenResponse> {
        return webClient
            .get()
            .uri("http://$SERVICE_NAME/user-remote-api/users/{loginToken}", loginToken)
            .retrieve()
            .bodyToMono(ResponseBodyBean::class.java)
            .mapNotNull(ResponseBodyBean<*>::data)
            .map { data: Any? -> JSONUtil.toBean(JSONUtil.parseObj(data), GetUserByLoginTokenResponse::class.java) }
    }

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    fun getRoleListByUserId(userId: @NotNull @Min(1L) Long): Mono<List<GetRoleListByUserIdSingleResponse>> {
        return webClient
            .get()
            .uri("http://$SERVICE_NAME/role-remote-api/roles/{userId}", userId)
            .retrieve()
            .bodyToMono(ResponseBodyBean::class.java)
            .mapNotNull(ResponseBodyBean<*>::data)
            .map { data: Any? ->
                JSONUtil.toList(
                    JSONUtil.parseObj(data).getJSONArray("roleList"),
                    GetRoleListByUserIdSingleResponse::class.java
                )
            }
    }

    /**
     * Get permission list by role id list
     *
     * @param payload the payload
     * @return the response body bean
     */
    fun getPermissionListByRoleIdList(
        payload: @Valid @NotNull GetPermissionListByRoleIdListPayload
    ): Mono<List<Permission>> {
        return webClient
            .get()
            .uri { uriBuilder: UriBuilder ->
                uriBuilder
                    .host(SERVICE_NAME)
                    .path("/permission-remote-api/permissions")
                    .queryParam("roleIdList", payload.roleIdList.joinToString(","))
                    .queryParam("permissionTypeList", payload.permissionTypeList.joinToString(","))
                    .build()
            }
            .retrieve()
            .bodyToMono(ResponseBodyBean::class.java)
            .mapNotNull(ResponseBodyBean<*>::data)
            .map { data: Any? ->
                JSONUtil.toList(
                    JSONUtil.parseObj(data).getJSONArray("permissionList"),
                    Permission::class.java
                )
            }
    }

    /**
     * Parse JWT.
     *
     * @param authorization the authorization
     * @return the mono
     */
    fun parse(authorization: @NotBlank String): Mono<ParseJwtResponse> {
        return webClient
            .get()
            .uri("http://auth-center/jwt-remote-api/parse")
            .headers { httpHeaders: HttpHeaders -> httpHeaders[HttpHeaders.AUTHORIZATION] = authorization }
            .retrieve()
            .bodyToMono(ResponseBodyBean::class.java)
            .mapNotNull(ResponseBodyBean<*>::data)
            .map { data: Any? -> JSONUtil.toBean(JSONUtil.parseObj(data), ParseJwtResponse::class.java) }
    }
}
