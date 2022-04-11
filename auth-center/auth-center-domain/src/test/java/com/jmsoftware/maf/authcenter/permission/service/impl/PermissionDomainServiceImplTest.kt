package com.jmsoftware.maf.authcenter.permission.service.impl

import com.jmsoftware.maf.authcenter.permission.mapper.PermissionMapper
import com.jmsoftware.maf.authcenter.permission.persistence.Permission
import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

/**
 * # PermissionDomainServiceImplTest
 *
 * Description: PermissionDomainServiceImplTest, change description here.
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
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 4/4/2022 8:40 AM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito with JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito with JUnit 5</a>
 */
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class PermissionDomainServiceImplTest {
    companion object {
        private val log = logger()
    }

    @InjectMocks
    private lateinit var permissionDomainService: PermissionDomainServiceImpl

    @Mock
    private lateinit var permissionMapper: PermissionMapper

    @BeforeEach
    fun setUp() {
        log.info("{} setUp", this.javaClass.simpleName)
    }

    @AfterEach
    fun tearDown() {
        log.info("{} tearDown", this.javaClass.simpleName)
    }

    @Test
    fun getPermissionListByRoleIdList() {
        val permission = Permission()
        permission.id = 1
        `when`(permissionMapper.selectPermissionListByRoleIdList(anyList(), anyList()))
            .thenReturn(listOf(permission))
        val permissionList = permissionDomainService.getPermissionListByRoleIdList(listOf(), listOf())
        log.info("permissionList: $permissionList")
        verify(permissionMapper)
            .selectPermissionListByRoleIdList(anyList(), anyList())
        assertNotNull(permissionList)
    }
}
