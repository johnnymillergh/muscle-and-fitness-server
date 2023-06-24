package com.jmsoftware.maf.apigateway.security.impl

import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.http.HttpStatus.NETWORK_AUTHENTICATION_REQUIRED
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.authentication.BadCredentialsException
import reactor.core.publisher.Mono

/**
 * # ServerAuthenticationEntryPointImplTest
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 5/28/22 2:17 PM
 **/
@ExtendWith(MockitoExtension::class)
class ServerAuthenticationEntryPointImplTest {
    @InjectMocks
    private lateinit var entryPoint: ServerAuthenticationEntryPointImpl

    @Mock
    private lateinit var responseUtil: ResponseUtil

    @Test
    fun commence() {
        val serverWebExchange = MockServerWebExchange.builder(
            MockServerHttpRequest.get("/mock").build()
        ).build()
        val message = "Bad credentials"
        `when`(responseUtil.renderJson(serverWebExchange, NETWORK_AUTHENTICATION_REQUIRED, message, null))
            .thenReturn(Mono.empty())
        entryPoint.commence(
            serverWebExchange,
            BadCredentialsException(message)
        ).block()
        verify(responseUtil, times(1))
            .renderJson(serverWebExchange, NETWORK_AUTHENTICATION_REQUIRED, message, null)
    }
}
