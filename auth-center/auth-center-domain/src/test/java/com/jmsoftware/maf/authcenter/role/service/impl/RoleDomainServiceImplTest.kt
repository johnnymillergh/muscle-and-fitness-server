package com.jmsoftware.maf.authcenter.role.service.impl

import cn.hutool.core.collection.CollUtil
import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.authcenter.role.RoleExcelBean
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper
import com.jmsoftware.maf.authcenter.role.persistence.Role
import com.jmsoftware.maf.common.exception.InternalServerException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
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
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import javax.validation.Validator

/**
 * # RoleDomainServiceImplTest
 *
 * Description: RoleDomainServiceImplTest, change description here.
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
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 4/4/2022 8:40 AM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito with JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito with JUnit 5</a>
 * <a href='https://github.com/baomidou/mybatis-plus/blob/master/mybatis-plus-extension/src/test/kotlin/com/baomidou/mybatisplus/test/kotlin/WrapperTest.kt'>WrapperTest.kt - MyBatis Plus</a>
 */
@Suppress("unused")
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class RoleDomainServiceImplTest {
    companion object {
        private val log = logger()
    }

    @Spy
    @InjectMocks
    private lateinit var roleDomainService: RoleDomainServiceImpl

    @Mock
    private lateinit var roleMapper: RoleMapper

    @Mock
    private lateinit var mafProjectProperties: MafProjectProperties

    @Mock
    private lateinit var mafConfigurationProperties: MafConfigurationProperties

    @Mock
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    private lateinit var valueOperations: ValueOperations<String, String>

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var validator: Validator

    /**
     * Set up
     *
     * @see <a href='https://github.com/baomidou/mybatis-plus/blob/master/mybatis-plus-extension/src/test/kotlin/com/baomidou/mybatisplus/test/kotlin/WrapperTest.kt'>WrapperTest.kt - MyBatis Plus</a>
     */
    @BeforeEach
    fun setUp() {
        log.info("${this.javaClass.simpleName} setUp")
        TableInfoHelper.initTableInfo(MapperBuilderAssistant(MybatisConfiguration(), ""), Role::class.java)
    }

    @AfterEach
    fun tearDown() {
        log.info("${this.javaClass.simpleName} tearDown")
        TableInfoHelper.remove(Role::class.java)
    }

    @Test
    fun getRoleList() {
        doReturn(roleMapper).`when`(roleDomainService).baseMapper
        `when`(redisTemplate.hasKey(anyString())).thenReturn(false)
        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
        val response = roleDomainService.getRoleList(1L)
        log.info("roleListResponse: $response")
        assertNotNull(response)
        assertTrue(CollUtil.isEmpty(response.roleList))
    }

    @Test
    fun roleListByUserId() {
        doReturn(roleMapper).`when`(roleDomainService).baseMapper
        val roleList = roleDomainService.getRoleListByUserId(1L)
        log.info("roleList: $roleList")
        assertNotNull(roleList)
        assertTrue(CollUtil.isEmpty(roleList))
    }

    @Test
    fun checkAdmin() {
        doReturn(roleMapper).`when`(roleDomainService).baseMapper
        val admin = roleDomainService.checkAdmin(listOf(1L))
        log.info("admin: $admin")
        assertFalse(admin)
    }

    @Test
    fun listForExporting() {
        doReturn(roleMapper).`when`(roleDomainService).baseMapper
        val thrownException = assertThrows(NullPointerException::class.java) { roleDomainService.getListForExporting() }
        log.warn("thrownException for listForExporting(): $thrownException")
    }

    @Test
    fun validateBeforeAddToBeanList() {
        val roleExcelBean = RoleExcelBean()
        roleDomainService.validateBeforeAddToBeanList(
            listOf(roleExcelBean),
            roleExcelBean,
            0
        )
    }

    @Test
    fun save() {
        val thrownException = assertThrows(InternalServerException::class.java) { roleDomainService.save(listOf()) }
        log.warn("thrownException for save(): $thrownException")
    }
}
