package com.jmsoftware.maf.authcenter.permission.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.authcenter.permission.configuration.PermissionConfiguration
import com.jmsoftware.maf.authcenter.permission.converter.PermissionMapStructMapper.Companion.INSTANCE
import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse
import com.jmsoftware.maf.authcenter.permission.response.ServiceInfo
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService
import com.jmsoftware.maf.authcenter.permission.service.PermissionService
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType.BUTTON
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse
import com.jmsoftware.maf.common.util.Slf4j
import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * # PermissionServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/18/2022 11:37 PM
 */
@Slf4j
@Service
class PermissionServiceImpl(
    private val permissionDomainService: PermissionDomainService,
    private val roleDomainService: RoleDomainService,
    private val discoveryClient: DiscoveryClient,
    private val restTemplate: RestTemplate,
    private val permissionConfiguration: PermissionConfiguration,
    private val objectMapper: ObjectMapper,
) : PermissionService {
    override fun getPermissionListByRoleIdList(
        payload: GetPermissionListByRoleIdListPayload
    ): GetPermissionListByRoleIdListResponse {
        val adminRole = roleDomainService.checkAdmin(payload.roleIdList)
        if (adminRole) {
            log.warn("Admin role checked. The role can access any resources")
            val permission = Permission()
            permission.url = "/**"
            permission.type = BUTTON.type
            permission.permissionExpression = "admin-permission"
            permission.method = "*"
            return GetPermissionListByRoleIdListResponse(
                listOf(permission)
            )
        }
        return GetPermissionListByRoleIdListResponse(
            INSTANCE.ofList(
                permissionDomainService.getPermissionListByRoleIdList(
                    payload.roleIdList, payload.permissionTypeList
                )
            )
        )
    }

    @Suppress("HttpUrlsUsage")
    override fun getServicesInfo(): GetServicesInfoResponse {
        val serviceIdList = discoveryClient.services
        log.info("Getting service info from Service ID list: $serviceIdList, ignored service ID: ${permissionConfiguration.ignoredServiceIds}")
        val response = GetServicesInfoResponse(
            serviceIdList.stream()
                .filter { serviceId: String ->
                    !permissionConfiguration.ignoredServiceIds.contains(serviceId)
                }
                .parallel()
                .map { serviceId: String ->
                    val responseBodyBean = restTemplate.getForObject(
                        "http://$serviceId/http-api-resources", ResponseBodyBean::class.java,
                    )!!
                    val httpApiResourcesResponse = objectMapper.convertValue(
                        responseBodyBean.data!!,
                        HttpApiResourcesResponse::class.java
                    )
                    ServiceInfo(
                        serviceId,
                        httpApiResourcesResponse
                    ).apply {
                        log.atDebug().log { "Added serviceInfo: $this" }
                    }
                }
                .toList()
        )
        if (response.list.isEmpty()) {
            log.warn("Got am empty ServiceInfo list")
        }
        return response
    }
}
