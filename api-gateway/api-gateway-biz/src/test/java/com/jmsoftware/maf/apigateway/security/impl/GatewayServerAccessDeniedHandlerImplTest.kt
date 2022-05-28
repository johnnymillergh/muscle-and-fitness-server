package com.jmsoftware.maf.apigateway.security.impl

import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.security.access.AccessDeniedException
import reactor.core.publisher.Mono

/**
 * # GatewayServerAccessDeniedHandlerImplTest
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 5/27/22 9:45 PM
 **/
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class GatewayServerAccessDeniedHandlerImplTest {
    @InjectMocks
    lateinit var handler: GatewayServerAccessDeniedHandlerImpl

    @Mock
    @Suppress("unused")
    lateinit var responseUtil: ResponseUtil

    @Test
    fun handle() {
        val serverWebExchange = MockServerWebExchange.builder(
            MockServerHttpRequest.get("/mock").build()
        ).build()
        val message = "Access denied for mocking"
        `when`(
            responseUtil.renderJson(
                serverWebExchange,
                HttpStatus.FORBIDDEN,
                message,
                null
            )
        ).thenReturn(Mono.empty())
        val voidMono = handler.handle(serverWebExchange, AccessDeniedException(message))
        assertNotNull(voidMono)
        voidMono.block()
    }
}
