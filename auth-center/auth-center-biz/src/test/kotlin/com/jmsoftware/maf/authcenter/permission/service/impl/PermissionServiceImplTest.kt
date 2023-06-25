package com.jmsoftware.maf.authcenter.permission.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.authcenter.permission.configuration.PermissionConfiguration
import com.jmsoftware.maf.authcenter.permission.persistence.Permission
import com.jmsoftware.maf.authcenter.permission.response.ServiceInfo
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType.BUTTON
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse
import com.jmsoftware.maf.common.util.Slf4j
import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.client.RestTemplate

/**
 * # PermissionServiceImplTest
 *
 * Description: PermissionServiceImplTest, change description here.
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
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 4/3/22 10:19 PM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito with JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito with JUnit 5</a>
 */
@Slf4j
@Suppress("unused")
@ExtendWith(MockitoExtension::class)
class PermissionServiceImplTest {
    @InjectMocks
    lateinit var permissionService: PermissionServiceImpl

    @Mock
    lateinit var permissionDomainService: PermissionDomainService

    @Mock
    lateinit var roleDomainService: RoleDomainService

    @Mock
    lateinit var discoveryClient: DiscoveryClient

    @Mock
    lateinit var restTemplate: RestTemplate

    @Mock
    lateinit var permissionConfiguration: PermissionConfiguration

    @Mock
    lateinit var objectMapper: ObjectMapper

    @Test
    fun getPermissionListByRoleIdList_whenItsNonAdmin_thenReturnConfiguredPermission() {
        whenever(roleDomainService.checkAdmin(anyList())).thenReturn(false)
        val permission = Permission().apply {
            this.url = "/fake/permissions"
            this.method = "GET"
            this.type = BUTTON.type
            this.permissionExpression = "FakePermissionExpression"
        }
        whenever(permissionDomainService.getPermissionListByRoleIdList(anyList(), anyList()))
            .thenReturn(listOf(permission))
        val payload = GetPermissionListByRoleIdListPayload()
        payload.roleIdList = listOf(1L)
        payload.permissionTypeList = listOf(BUTTON)
        val response = assertDoesNotThrow { permissionService.getPermissionListByRoleIdList(payload) }
        assertNotNull(response)
        assertEquals(1, response.permissionList.size)
        val firstPermission = response.permissionList.first()
        assertNotNull(firstPermission)
        assertEquals(permission.url, firstPermission.url)
        assertEquals(permission.method, firstPermission.method)
        assertEquals(permission.type, firstPermission.type)
        assertEquals(permission.permissionExpression, firstPermission.permissionExpression)
        verify(roleDomainService).checkAdmin(anyList())
        verify(permissionDomainService).getPermissionListByRoleIdList(anyList(), anyList())
    }

    @Test
    fun getPermissionListByRoleIdList_whenItsAdmin_thenReturnAllPermissions() {
        whenever(roleDomainService.checkAdmin(anyList())).thenReturn(true)
        val payload = GetPermissionListByRoleIdListPayload()
        payload.roleIdList = listOf(1L)
        payload.permissionTypeList = listOf(BUTTON)
        val response = assertDoesNotThrow { permissionService.getPermissionListByRoleIdList(payload) }
        assertNotNull(response)
        assertEquals(1, response.permissionList.size)
        val firstPermission = response.permissionList.first()
        assertEquals("/**", firstPermission.url)
        assertEquals(BUTTON.type, firstPermission.type)
        assertEquals("admin-permission", firstPermission.permissionExpression)
        assertEquals("*", firstPermission.method)
        verify(roleDomainService).checkAdmin(anyList())
        verify(permissionDomainService, never()).getPermissionListByRoleIdList(anyList(), anyList())
    }

    @Test
    fun getServicesInfo_when5ServicesInTotal_andIgnore2Service_thenReturn3ServiceInfo() {
        whenever(discoveryClient.services)
            .thenReturn(listOf("auth-center", "oss-center", "maf-mis", "api-gateway", "spring-boot-admin"))
        whenever(permissionConfiguration.ignoredServiceIds).thenReturn(setOf("api-gateway", "spring-boot-admin"))
        val httpApiResourcesResponse = HttpApiResourcesResponse()
        val element = HttpApiResourcesResponse.HttpApiResource()
        element.method = RequestMethod.GET
        element.urlPattern = "/api/v1/**"
        httpApiResourcesResponse.list.add(element)
        whenever(objectMapper.convertValue(any(), any<Class<Any>>())).thenReturn(httpApiResourcesResponse)
        whenever(restTemplate.getForObject(anyString(), any<Class<Any>>()))
            .thenReturn(ResponseBodyBean.ofSuccess(httpApiResourcesResponse))
        val response = assertDoesNotThrow { permissionService.getServicesInfo() }
        log.info("Services info: $response")
        assertEquals(3, response.list.size)
        val serviceIds = response.list.map(ServiceInfo::serviceId).toSet()
        assertTrue(serviceIds.containsAll(setOf("auth-center", "maf-mis", "oss-center")))
        verify(discoveryClient).services
        verify(restTemplate, times(3)).getForObject(anyString(), any<Class<Any>>())
    }

    @Test
    fun getServicesInfo_when5ServicesInTotal_andIgnore2Service_thenReturn3ServiceInfo1() {
        whenever(discoveryClient.services).thenReturn(listOf("api-gateway", "spring-boot-admin"))
        whenever(permissionConfiguration.ignoredServiceIds).thenReturn(setOf("api-gateway", "spring-boot-admin"))
        val response = assertDoesNotThrow { permissionService.getServicesInfo() }
        log.info("Services info: $response")
        assertEquals(0, response.list.size)
        val serviceIds = response.list.map(ServiceInfo::serviceId).toSet()
        assertFalse(serviceIds.containsAll(setOf("api-gateway", "spring-boot-admin")))
        verify(discoveryClient).services
        verify(restTemplate, never()).getForObject(anyString(), any<Class<Any>>())
    }
}
