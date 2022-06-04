@file:Suppress("HttpUrlsUsage")

package com.jmsoftware.maf.authcenter.permission.service.impl

import cn.hutool.core.collection.CollUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Lists
import com.jmsoftware.maf.authcenter.permission.configuration.PermissionConfiguration
import com.jmsoftware.maf.authcenter.permission.converter.PermissionMapStructMapper
import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse
import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse.ServiceInfo
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService
import com.jmsoftware.maf.authcenter.permission.service.PermissionService
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse
import com.jmsoftware.maf.common.domain.authcenter.permission.Permission
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.function.lazyDebug
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * # PermissionServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/18/2022 11:37 PM
 */
@Service
class PermissionServiceImpl(
    private val permissionDomainService: PermissionDomainService,
    private val roleDomainService: RoleDomainService,
    private val discoveryClient: DiscoveryClient,
    private val restTemplate: RestTemplate,
    private val permissionConfiguration: PermissionConfiguration,
    private val objectMapper: ObjectMapper,
) : PermissionService {
    companion object {
        private val log = logger()
    }

    override fun getPermissionListByRoleIdList(
        payload: GetPermissionListByRoleIdListPayload
    ): GetPermissionListByRoleIdListResponse {
        val adminRole = roleDomainService.checkAdmin(payload.roleIdList)
        val response = GetPermissionListByRoleIdListResponse()
        if (adminRole) {
            log.warn("Admin role checked. The role can access any resources")
            val permission = Permission()
            permission.url = "/**"
            permission.type = PermissionType.BUTTON.type
            permission.permissionExpression = "admin-permission"
            permission.method = "*"
            response.permissionList.add(permission)
            return response
        }
        return response.apply {
            this.permissionList.addAll(
                permissionDomainService.getPermissionListByRoleIdList(
                    payload.roleIdList, payload.permissionTypeList
                ).stream()
                    .map { permission -> PermissionMapStructMapper.INSTANCE.of(permission) }
                    .toList()
            )
        }
    }

    override fun getServicesInfo(): GetServicesInfoResponse {
        val serviceIdList = discoveryClient.services
        log.info("Getting service info from Service ID list: $serviceIdList")
        val response = GetServicesInfoResponse()
        log.info("Ignored service ID: ${permissionConfiguration.ignoredServiceIds}")
        response.list = serviceIdList.stream()
            .filter { serviceId: String ->
                !CollUtil.contains(
                    permissionConfiguration.ignoredServiceIds,
                    serviceId
                )
            }
            .parallel()
            .map { serviceId: String ->
                val responseBodyBean = restTemplate.getForObject(
                    "http://$serviceId/http-api-resources", ResponseBodyBean::class.java,
                )!!
                val httpApiResourcesResponse = objectMapper.convertValue(
                    Objects.requireNonNull(responseBodyBean).data,
                    HttpApiResourcesResponse::class.java
                )
                val serviceInfo = ServiceInfo()
                serviceInfo.serviceId = serviceId
                serviceInfo.httpApiResources = httpApiResourcesResponse
                lazyDebug(log) { "Added serviceInfo: $serviceInfo" }
                serviceInfo
            }.toList()
        if (CollUtil.isEmpty(response.list)) {
            log.warn("Got am empty ServiceInfo list")
        }
        return response
    }
}
