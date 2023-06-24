package com.jmsoftware.maf.apigateway.remote.impl

import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClient
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono

/**
 * # AuthCenterWebClientServiceImplTest
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/3/22 10:26 AM
 **/
@ExtendWith(MockitoExtension::class)
class AuthCenterWebClientServiceImplTest {
    @InjectMocks
    private lateinit var authCenter: AuthCenterWebClientServiceImpl

    @Mock
    private lateinit var authCenterWebClient: AuthCenterWebClient

    @Test
    fun getUserByLoginToken() {
        whenever(authCenterWebClient.getUserByLoginToken(anyString())).thenReturn(Mono.just(GetUserByLoginTokenResponse()))
        assertNotNull(authCenter.getUserByLoginToken("FakeLoginToken").block())
    }

    @Test
    fun getRoleListByUserId() {
        whenever(authCenterWebClient.getRoleListByUserId(anyLong())).thenReturn(
            Mono.just(
                listOf(
                    GetRoleListByUserIdSingleResponse()
                )
            )
        )
        assertNotEquals(authCenter.getRoleListByUserId(1L).block()?.size, 0)
    }

    @Test
    fun getPermissionListByRoleIdList() {
        whenever(authCenterWebClient.getPermissionListByRoleIdList(any()))
            .thenReturn(Mono.just(listOf(Permission())))
        assertNotEquals(
            authCenter.getPermissionListByRoleIdList(GetPermissionListByRoleIdListPayload()).block()?.size, 0
        )
    }

    @Test
    fun parse() {
        whenever(authCenterWebClient.parse(anyString())).thenReturn(Mono.just(ParseJwtResponse()))
        assertNotNull(authCenter.parse("FakeJwt").block())
    }
}
