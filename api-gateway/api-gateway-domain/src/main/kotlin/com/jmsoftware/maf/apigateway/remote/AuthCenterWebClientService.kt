package com.jmsoftware.maf.apigateway.remote

import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Mono
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * # AuthCenterWebClientService
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 4/16/22 9:37 PM
 **/
@Validated
interface AuthCenterWebClientService {
    /**
     * Get user by login token
     *
     * @param loginToken
     * @return
     */
    fun getUserByLoginToken(loginToken: @NotBlank String): Mono<GetUserByLoginTokenResponse>

    /**
     * Get role list by user id
     *
     * @param userId
     * @return role list
     */
    fun getRoleListByUserId(userId: @NotNull @Min(1L) Long): Mono<List<GetRoleListByUserIdSingleResponse>>

    /**
     * Get permission list by role id list
     *
     * @param payload
     * @return permission list
     */
    fun getPermissionListByRoleIdList(
        payload: @Valid @NotNull GetPermissionListByRoleIdListPayload
    ): Mono<List<Permission>>

    /**
     * Parse
     *
     * @param authorization
     * @return jwt
     */
    fun parse(authorization: @NotBlank String): Mono<ParseJwtResponse>
}
