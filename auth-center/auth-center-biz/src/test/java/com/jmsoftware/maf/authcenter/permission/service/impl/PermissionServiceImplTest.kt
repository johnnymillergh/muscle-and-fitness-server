package com.jmsoftware.maf.authcenter.permission.service.impl

import cn.hutool.core.util.StrUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Lists
import com.jmsoftware.maf.authcenter.permission.configuration.PermissionConfiguration
import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse.ServiceInfo
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse
import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
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
@Suppress("unused")
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class PermissionServiceImplTest {
    companion object {
        private val log = logger()
    }

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
        `when`(roleDomainService.checkAdmin(anyList())).thenReturn(false)
        `when`(permissionDomainService.getPermissionListByRoleIdList(anyList(), anyList()))
            .thenReturn(Lists.newArrayList())
        val payload = GetPermissionListByRoleIdListPayload()
        payload.roleIdList = Lists.newArrayList()
        payload.permissionTypeList = Lists.newArrayList()
        val response = permissionService.getPermissionListByRoleIdList(payload)
        log.info("Permission list response: {}", response)
        verify(roleDomainService).checkAdmin(anyList())
        verify(permissionDomainService).getPermissionListByRoleIdList(anyList(), anyList())
        assertEquals(0, response.permissionList.size)
    }

    @Test
    fun getServicesInfo() {
        `when`(discoveryClient.services)
            .thenReturn(listOf("auth-center", "oss-center", "maf-mis", "api-gateway", "spring-boot-admin"))
        `when`(permissionConfiguration.ignoredServiceIds)
            .thenReturn(setOf("api-gateway", "spring-boot-admin"))
        val httpApiResourcesResponse = HttpApiResourcesResponse()
        val element = HttpApiResourcesResponse.HttpApiResource()
        element.method = RequestMethod.GET
        element.urlPattern = "/api/v1/**"
        httpApiResourcesResponse.list.add(element)
        `when`(objectMapper.convertValue(any(), any<Class<Any>>()))
            .thenReturn(httpApiResourcesResponse)
        `when`(restTemplate.getForObject(anyString(), any<Class<Any>>()))
            .thenReturn(ResponseBodyBean.ofSuccess(httpApiResourcesResponse))
        val servicesInfo = permissionService.getServicesInfo()
        log.info("Services info: {}", servicesInfo)
        verify(discoveryClient).services
        assertNotEquals(0, servicesInfo.list.size)
        assertTrue(
            servicesInfo.list
                .stream()
                .anyMatch { service: ServiceInfo ->
                    StrUtil.equalsAnyIgnoreCase(service.serviceId, "auth-center")
                }
        )
    }
}
