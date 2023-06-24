package com.jmsoftware.maf.authcenter.user.service.impl

import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.jmsoftware.maf.authcenter.security.service.JwtService
import com.jmsoftware.maf.authcenter.user.mapper.UserMapper
import com.jmsoftware.maf.authcenter.user.payload.GetUserPageListPayload
import com.jmsoftware.maf.authcenter.user.payload.GetUserStatusPayload
import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.authcenter.user.service.UserRoleDomainService
import com.jmsoftware.maf.common.domain.authcenter.user.LoginPayload
import com.jmsoftware.maf.common.domain.authcenter.user.SignupPayload
import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import jakarta.servlet.http.HttpServletRequest
import org.apache.ibatis.builder.MapperBuilderAssistant
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
import org.springframework.context.MessageSource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * # UserDomainServiceImplTest
 *
 * Description: UserDomainServiceImplTest, change description here.
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
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 7:46 AM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito with JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito with JUnit 5</a>
 */
@Suppress("unused")
@ExtendWith(MockitoExtension::class)
class UserDomainServiceImplTest {
    companion object {
        private const val USERNAME = "ijohnnymiller"
        private val log = logger()
    }

    @Spy
    @InjectMocks
    lateinit var userDomainService: UserDomainServiceImpl

    @Mock
    lateinit var userMapper: UserMapper

    @Mock
    lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Mock
    lateinit var jwtService: JwtService

    @Mock
    lateinit var messageSource: MessageSource

    @Mock
    lateinit var mafProjectProperties: MafProjectProperties

    @Mock
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    lateinit var userRoleDomainService: UserRoleDomainService

    @Mock
    lateinit var mafConfigurationProperties: MafConfigurationProperties

    @Mock
    lateinit var httpServletRequest: HttpServletRequest

    @BeforeEach
    fun setUp() {
        log.info("${this.javaClass.simpleName} setUp")
        TableInfoHelper.initTableInfo(MapperBuilderAssistant(MybatisConfiguration(), ""), User::class.java)
    }

    @AfterEach
    fun tearDown() {
        log.info("${this.javaClass.simpleName} tearDown")
        TableInfoHelper.remove(User::class.java)
    }

    @Test
    fun getUserByLoginToken() {
        doReturn(userMapper).`when`(userDomainService).baseMapper
        val user = userDomainService.getUserByLoginToken(USERNAME)
        log.info("User: $user")
        assertNull(user)
    }

    @Test
    fun saveUserForSignup() {
        doReturn(userMapper).`when`(userDomainService).baseMapper
        `when`(bCryptPasswordEncoder.encode(anyString())).thenReturn("encrypted-password-for-unit-test")
        `when`(mafConfigurationProperties.guestUserRole).thenReturn("guest")
        val payload = SignupPayload()
        payload.username = USERNAME
        payload.email = "johnnysviva@outlook.com"
        payload.password = "password-for-unit-test"
        val signupResponse = userDomainService.saveUserForSignup(payload)
        log.info("Signup response: $signupResponse")
        assertNotNull(signupResponse)
    }

    @Test
    fun login() {
        doReturn(userMapper).`when`(userDomainService).baseMapper
        val payload = LoginPayload()
        payload.loginToken = USERNAME
        val thrownException = assertThrows(SecurityException::class.java) { userDomainService.login(payload) }
        log.warn("Thrown exception: ${thrownException.message}")
    }

    @Test
    fun logout() {
        val logout = userDomainService.logout(httpServletRequest)
        assertTrue(logout)
    }

    @Test
    fun getUserStatus() {
        val payload = GetUserStatusPayload()
        payload.status = UserStatus.ENABLED
        val thrownException =
            assertThrows(IllegalStateException::class.java) { userDomainService.getUserStatus(payload) }
        log.warn("Thrown exception: ${thrownException.message}")
    }

    @Test
    fun getUserPageList() {
        doReturn(userMapper).`when`(userDomainService).baseMapper
        val response = userDomainService.getUserPageList(GetUserPageListPayload())
        log.info("User page list: $response")
        assertNotNull(response)
        assertTrue(CollUtil.isEmpty(response.list))
    }
}
