package com.jmsoftware.maf.authcenter.user.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.StrUtil
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
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.util.UserUtil
import org.apache.ibatis.builder.MapperBuilderAssistant
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.context.MessageSource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.servlet.http.HttpServletRequest

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
@Execution(ExecutionMode.CONCURRENT)
internal class UserDomainServiceImplTest {
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
    lateinit var httpServletRequest : HttpServletRequest

    @BeforeEach
    fun setUp() {
        log.info("{} setUp", this.javaClass.simpleName)
        TableInfoHelper.initTableInfo(MapperBuilderAssistant(MybatisConfiguration(), ""), User::class.java)
    }

    @AfterEach
    fun tearDown() {
        log.info("{} tearDown", this.javaClass.simpleName)
    }

    @Test
    fun getUserByLoginToken() {
        doReturn(userMapper).`when`(userDomainService).baseMapper
        val user = userDomainService.getUserByLoginToken(USERNAME)
        log.info("User: {}", user)
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
        log.info("Signup response: {}", signupResponse)
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
        mockStatic(UserUtil::class.java).use {
            it.`when`<Any> { UserUtil.getCurrentUsername() }.thenReturn("ijohnnymiller")
            val payload = GetUserStatusPayload()
            payload.status = 1.toByte()
            payload.status2 = 0.toByte()
            val userStatus = userDomainService.getUserStatus(payload)
            log.info("User status: {}", userStatus)
            assertNotNull(userStatus)
            assertTrue(StrUtil.isNotBlank(userStatus))
        }
    }

    @Test
    fun getUserPageList() {
        doReturn(userMapper).`when`(userDomainService).baseMapper
        val response = userDomainService.getUserPageList(GetUserPageListPayload())
        log.info("User page list: {}", response)
        assertNotNull(response)
        assertTrue(CollUtil.isEmpty(response.list))
    }
}
