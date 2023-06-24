package com.jmsoftware.maf.authcenter.security.service.impl

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.JwtConfigurationProperties
import io.jsonwebtoken.Claims
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.util.concurrent.atomic.AtomicReference


/**
 * # JwtServiceImplTest
 *
 * Description: JwtServiceImplTest, change description here.
 *
 * ## Mockito JUnit 5 Extension
 *
 * There is also a Mockito extension for JUnit 5 that will make the initialization even simpler.
 *
 * **Pros:**
 *
 *  * No need to call `MockitoAnnotations.openMocks()`
 *  * Validates framework usage and detects incorrect stubbing
 *  * Easy to create mocks
 *  * Very readable
 *
 * **Cons:**
 *
 *  * Need an extra dependency on `org.mockito:mockito-junit-jupiter`, which has been included by Spring.
 * So we don&#39;t have to worry about this.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 9:29 PM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito with JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito with JUnit 5</a>
 */
@ExtendWith(MockitoExtension::class)
class JwtServiceImplTest {
    companion object {
        private val log = logger()
        private const val USER_ID = 1L
        private const val USERNAME = "ijohnnymiller"
        private const val JWT = "eyJhbGciOiJIUzM4NCJ9" +
                ".eyJqdGkiOiIxIiwic3ViIjoiaWpvaG5ueW1pbGxlciIsImlhdCI6MTY0OTA0NTgzMiwicm9sZXMiOltdfQ" +
                ".lMQDOTnNsZTaopDBf7O_4zlhPJTZmGB3v6MpjmWnPuMEb2Aj-bXciDh4m2pAYtEp"
    }

    @Spy
    @InjectMocks
    private lateinit var jwtService: JwtServiceImpl

    @Mock
    private lateinit var jwtConfigurationProperties: JwtConfigurationProperties

    @Mock
    private lateinit var redisTemplate: RedisTemplate<Any, Any>

    @Mock
    private lateinit var valueOperations: ValueOperations<Any, Any>

    @Mock
    private lateinit var httpServletRequest: HttpServletRequest

    @BeforeEach
    fun setUp() {
        log.info("${this.javaClass.simpleName} setUp")
        `when`(jwtConfigurationProperties.signingKey)
            .thenReturn("signing-key-for-unit-test-only-do-not-use-in-production")
        jwtService.init()
    }

    @AfterEach
    fun tearDown() {
        log.info("${this.javaClass.simpleName} tearDown")
    }

    @Test
    fun createJwt() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        doNothing().`when`(valueOperations)[anyString(), anyString(), anyLong()] = any()
        val jwt = jwtService.createJwt(true, USER_ID, USERNAME, listOf(), listOf())
        assertNotEquals(0, jwt.length)
    }

    @Test
    fun parseJwt() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.operations).thenReturn(redisTemplate)
        val claimsAtomicReference = AtomicReference<Claims>()
        assertThrows(SecurityException::class.java) { claimsAtomicReference.set(jwtService.parseJwt(JWT)) }
        log.info("Parse JWT. claimsAtomicReference: ${claimsAtomicReference.hashCode()}")
        assertNotNull(claimsAtomicReference)
        assertNull(claimsAtomicReference.get())
    }

    @Test
    fun invalidateJwt() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.operations).thenReturn(redisTemplate)
        doReturn(JWT).`when`(jwtService).getJwtFromRequest(httpServletRequest)
        assertThrows(SecurityException::class.java) { jwtService.invalidateJwt(httpServletRequest) }
    }

    @Test
    fun usernameFromJwt() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.operations).thenReturn(redisTemplate)
        assertThrows(SecurityException::class.java) { jwtService.getUsernameFromJwt(JWT) }
    }

    @Test
    fun getUsernameFromRequest() {
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        `when`(valueOperations.operations).thenReturn(redisTemplate)
        doReturn(JWT).`when`(jwtService).getJwtFromRequest(httpServletRequest)
        val thrownException =
            assertThrows(SecurityException::class.java) { jwtService.getUsernameFromRequest(httpServletRequest) }
        log.warn("Thrown exception: ${thrownException.message}")
    }

    @Test
    fun jwtFromRequest() {
        val jwt = jwtService.getJwtFromRequest(httpServletRequest)
        log.info("JWT: $jwt")
        assertTrue(StrUtil.isNotBlank(jwt))
    }

    @Test
    fun parse() {
        val thrownException = assertThrows(SecurityException::class.java) { jwtService.parse(httpServletRequest) }
        log.warn("Thrown exception: ${thrownException.message}")
    }
}
