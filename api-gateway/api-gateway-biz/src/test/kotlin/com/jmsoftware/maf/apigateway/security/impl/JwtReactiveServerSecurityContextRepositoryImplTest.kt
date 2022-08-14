package com.jmsoftware.maf.apigateway.security.impl

import com.google.common.net.HttpHeaders.AUTHORIZATION
import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClientService
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpHeaders
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import reactor.core.publisher.Mono

/**
 * # JwtReactiveServerSecurityContextRepositoryImplTest
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 5/28/22 2:17 PM
 **/
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class JwtReactiveServerSecurityContextRepositoryImplTest {
    companion object {
        private val log = logger()
    }

    @InjectMocks
    private lateinit var repository: JwtReactiveServerSecurityContextRepositoryImpl

    @Mock
    private lateinit var mafConfigurationProperties: MafConfigurationProperties

    @Mock
    private lateinit var authenticationManager: ReactiveAuthenticationManager

    @Mock
    private lateinit var authCenterWebClientService: AuthCenterWebClientService

    @Mock
    private lateinit var securityContext: SecurityContext

    private val serverWebExchange1 = MockServerWebExchange.builder(
        MockServerHttpRequest.get("/mock").build()
    ).build()

    private val serverWebExchange2 = MockServerWebExchange.builder(
        MockServerHttpRequest.get("/mock")
            .headers(HttpHeaders().apply {
                this.set(AUTHORIZATION, "Bearer fake-token")
            })
            .build()
    ).build()

    @Test
    fun save() {
        assertThrows<UnsupportedOperationException> {
            repository.save(serverWebExchange1, securityContext).block()
        }
    }

    @Test
    fun load_whenTheUrlCanBeIgnored_thenDontDoAuthentication() {
        `when`(mafConfigurationProperties.flattenIgnoredUrls()).thenReturn(listOf("/mock"))
        val securityContext = repository.load(serverWebExchange1).block()
        assertNull(securityContext)
    }

    @Test
    fun load_whenAuthorizationNotExistsInHttpHeaders_thenLoadingShouldFail() {
        `when`(mafConfigurationProperties.flattenIgnoredUrls()).thenReturn(listOf("/other-url"))
        val exception = assertThrows<RuntimeException> {
            repository.load(serverWebExchange1).block()
        }
        log.info("Exception occurred! {}", exception.message)
    }

    @Test
    fun load_whenAuthorizationExistsInHttpHeadersAndParseJwtResponseIsEmpty_thenReturningEmpty() {
        `when`(mafConfigurationProperties.flattenIgnoredUrls()).thenReturn(listOf("/other-url"))
        `when`(authCenterWebClientService.parse(anyString())).thenReturn(Mono.empty())
        val securityContext = repository.load(serverWebExchange2).block()
        assertNull(securityContext)
    }

    @Test
    fun load_whenAuthorizationExistsInHttpHeadersAndParseJwtResponseIsNotEmpty_thenReturningSecurityContext() {
        `when`(mafConfigurationProperties.flattenIgnoredUrls()).thenReturn(listOf("/other-url"))
        `when`(authCenterWebClientService.parse(anyString())).thenReturn(Mono.just(ParseJwtResponse().apply {
            this.username = "ijohnnymiller"
        }))
        `when`(authenticationManager.authenticate(any(Authentication::class.java))).thenReturn(
            Mono.just(
                TestingAuthenticationToken("ijohnnymiller", "fake-credentials")
            )
        )
        val securityContext = repository.load(serverWebExchange2).block()
        assertNotNull(securityContext)
    }
}
