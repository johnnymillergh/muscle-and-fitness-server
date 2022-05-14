package com.jmsoftware.maf.authcenter.user.service.impl

import com.baomidou.mybatisplus.core.MybatisConfiguration
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper
import com.jmsoftware.maf.authcenter.role.persistence.Role
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.authcenter.user.mapper.UserRoleMapper
import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.authcenter.user.persistence.UserRole
import com.jmsoftware.maf.common.exception.InternalServerException
import com.jmsoftware.maf.common.util.logger
import org.apache.ibatis.builder.MapperBuilderAssistant
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension

/**
 * # UserRoleDomainServiceImplTest
 *
 * Description: UserRoleDomainServiceImplTest, change description here.
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
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 10:23 AM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito with JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito with JUnit 5</a>
 */
@Suppress("unused")
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class UserRoleDomainServiceImplTest {
    companion object {
        private val log = logger()
    }

    @Spy
    @InjectMocks
    private lateinit var userRoleDomainService: UserRoleDomainServiceImpl

    @Mock
    private lateinit var userRoleMapper: UserRoleMapper

    @Mock
    private lateinit var roleMapper: RoleMapper

    @Mock
    private lateinit var roleDomainService: RoleDomainService

    @BeforeEach
    fun setUp() {
        log.info("${this.javaClass.simpleName} setUp")
        TableInfoHelper.initTableInfo(MapperBuilderAssistant(MybatisConfiguration(), ""), UserRole::class.java)
        TableInfoHelper.initTableInfo(MapperBuilderAssistant(MybatisConfiguration(), ""), Role::class.java)
    }

    @AfterEach
    fun tearDown() {
        log.info("${this.javaClass.simpleName} tearDown")
    }

    @Test
    fun assignRoleByRoleName() {
        `when`(roleDomainService.ktQuery()).thenReturn(KtQueryChainWrapper(roleMapper, Role::class.java))
        val user = User()
        user.username = "johnny"
        val thrownException = assertThrows(InternalServerException::class.java) {
            userRoleDomainService.assignRoleByRoleName(user, "ROLE_ADMIN_FOR_UNIT_TEST")
        }
        log.warn("Exception: ${thrownException.message}")
    }
}
