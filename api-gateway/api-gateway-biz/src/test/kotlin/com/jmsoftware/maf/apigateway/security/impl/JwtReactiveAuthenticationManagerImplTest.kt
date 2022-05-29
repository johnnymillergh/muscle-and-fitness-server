package com.jmsoftware.maf.apigateway.security.impl

import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClientService
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus
import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.TestingAuthenticationToken
import reactor.core.publisher.Mono

/**
 * # JwtReactiveAuthenticationManagerImplTest
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 5/27/22 9:45 PM
 **/
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class JwtReactiveAuthenticationManagerImplTest {
    companion object {
        private val log = logger()
    }

    @InjectMocks
    private lateinit var manager: JwtReactiveAuthenticationManagerImpl

    @Mock
    private lateinit var authCenterWebClientService: AuthCenterWebClientService

    @Test
    fun authenticate_whenUsernameIsBlank_thenAuthenticationShouldFail() {
        assertThrows<RuntimeException> {
            manager.authenticate(TestingAuthenticationToken("", "fake-credentials"))
                .doOnError { t: Throwable ->
                    log.info("Exception occurred! {}", t.message)
                }
                .block()
        }
    }

    @Test
    fun authenticate_whenUserNotFound_thenAuthenticationShouldFail() {
        `when`(authCenterWebClientService.getUserByLoginToken(anyString())).thenReturn(Mono.empty())
        assertThrows<RuntimeException> {
            manager.authenticate(TestingAuthenticationToken("ijohnnymiller", "fake-credentials"))
                .doOnError { t: Throwable ->
                    log.info("Exception occurred! {}", t.message)
                }
                .block()
        }
    }

    @Test
    fun authenticate_whenUserIsDisabled_thenAuthenticationShouldFail() {
        `when`(authCenterWebClientService.getUserByLoginToken(anyString())).thenReturn(
            Mono.just(
                GetUserByLoginTokenResponse()
            )
        )
        assertThrows<RuntimeException> {
            manager.authenticate(TestingAuthenticationToken("ijohnnymiller", "fake-credentials"))
                .doOnError { t: Throwable ->
                    log.info("Exception occurred! {}", t.message)
                }
                .block()
        }
    }

    @Test
    fun authenticate_whenUserIsEnabled_thenAuthenticationShouldPass() {
        val username = "ijohnnymiller"
        `when`(authCenterWebClientService.getUserByLoginToken(anyString())).thenReturn(
            Mono.just(
                GetUserByLoginTokenResponse().apply {
                    this.username = username
                    this.status = UserStatus.ENABLED.value
                }
            )
        )
        val result = manager.authenticate(TestingAuthenticationToken(username, "fake-credentials")).block()
        assertNotNull(result)
        assertNotNull(result?.principal)
        log.info("Result: $result")
    }
}
