package com.jmsoftware.maf.apigateway.security.impl

import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClientService
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.web.server.authorization.AuthorizationContext
import reactor.core.publisher.Mono

/**
 * # RbacReactiveAuthorizationManagerImplTest
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 5/28/22 2:17 PM
 **/
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class RbacReactiveAuthorizationManagerImplTest {
    companion object {
        private val log = logger()
    }

    @InjectMocks
    private lateinit var manager: RbacReactiveAuthorizationManagerImpl

    @Mock
    private lateinit var authCenterWebClientService: AuthCenterWebClientService

    @Test
    fun check_whenTheUrlMatches_thenPermissionShouldBeGranted() {
        `when`(authCenterWebClientService.getRoleListByUserId(anyLong())).thenReturn(Mono.just(listOf(
            GetRoleListByUserIdSingleResponse().apply {
                this.id = 1L
                this.name = "fake-admin"
            }
        )))
        `when`(authCenterWebClientService.getPermissionListByRoleIdList(any()))
            .thenReturn(Mono.just(listOf(Permission().apply {
                this.method = "GET"
                this.url = "/mock"
                this.type = PermissionType.BUTTON.type
                this.permissionExpression = "fake-permission-expression"
            })))
        val authorizationDecision = manager.check(
            Mono.just(TestingAuthenticationToken(UserPrincipal.create(GetUserByLoginTokenResponse().apply {
                this.id = 1L
                this.username = "ijohnnymiller"
            }), "fake-credentials")),
            AuthorizationContext(
                MockServerWebExchange.builder(
                    MockServerHttpRequest.get("/mock").build()
                ).build()
            )
        ).block()
        assertTrue(authorizationDecision!!.isGranted)
    }
}
