package com.jmsoftware.maf.apigateway.remote.impl

import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClient
import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClientService
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * # AuthCenterWebClientServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 4/16/22 9:40 PM
 **/
@Service
class AuthCenterWebClientServiceImpl(
    private val authCenterWebClient: AuthCenterWebClient
) : AuthCenterWebClientService {
    override fun getUserByLoginToken(loginToken: String): Mono<GetUserByLoginTokenResponse> =
        authCenterWebClient.getUserByLoginToken(loginToken)

    override fun getRoleListByUserId(userId: Long): Mono<List<GetRoleListByUserIdSingleResponse>> =
        authCenterWebClient.getRoleListByUserId(userId)

    override fun getPermissionListByRoleIdList(
        payload: GetPermissionListByRoleIdListPayload
    ): Mono<List<Permission>> = authCenterWebClient.getPermissionListByRoleIdList(payload)

    override fun parse(authorization: String): Mono<ParseJwtResponse> = authCenterWebClient.parse(authorization)
}
