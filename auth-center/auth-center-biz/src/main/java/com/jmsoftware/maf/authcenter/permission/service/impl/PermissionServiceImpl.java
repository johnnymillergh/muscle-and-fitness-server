package com.jmsoftware.maf.authcenter.permission.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jmsoftware.maf.authcenter.permission.configuration.PermissionConfiguration;
import com.jmsoftware.maf.authcenter.permission.converter.PermissionMapStructMapper;
import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse;
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService;
import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.jmsoftware.maf.springcloudstarter.function.Slf4j.lazyDebug;

/**
 * Description: PermissionServiceImpl, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/18/2022 11:37 PM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionDomainService permissionDomainService;
    private final RoleDomainService roleDomainService;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final PermissionConfiguration permissionConfiguration;

    @Override
    public GetPermissionListByRoleIdListResponse getPermissionListByRoleIdList(
            @Valid @NotNull GetPermissionListByRoleIdListPayload payload
    ) {
        val adminRole = this.roleDomainService.checkAdmin(payload.getRoleIdList());
        val response = new GetPermissionListByRoleIdListResponse();
        response.setPermissionList(Lists.newArrayList());
        if (adminRole) {
            log.warn("Admin role checked. The role can access any resources");
            val permission = new GetPermissionListByRoleIdListResponse.Permission();
            permission.setUrl("/**");
            permission.setType(PermissionType.BUTTON.getType());
            permission.setPermissionExpression("admin-permission");
            permission.setMethod("*");
            response.getPermissionList().add(permission);
            return response;
        }
        val permissionList =
                this.permissionDomainService.getPermissionListByRoleIdList(
                        payload.getRoleIdList(), payload.getPermissionTypeList());
        response.setPermissionList(
                permissionList
                        .stream()
                        .map(PermissionMapStructMapper.INSTANCE::of)
                        .collect(Collectors.toList())
        );
        return response;
    }

    @Override
    public GetServicesInfoResponse getServicesInfo() {
        val serviceIdList = this.discoveryClient.getServices();
        log.info("Getting service info from Service ID list: {}", serviceIdList);
        val response = new GetServicesInfoResponse();
        val mapper = new ObjectMapper();
        log.info("Ignored service ID: {}", this.permissionConfiguration.getIgnoredServiceIds());
        response.setList(
                serviceIdList.stream()
                        .filter(serviceId -> CollUtil.contains(
                                this.permissionConfiguration.getIgnoredServiceIds(),
                                serviceId
                        ))
                        .parallel()
                        .map(serviceId -> {
                            val responseBodyBean = this.restTemplate.getForObject(
                                    format("http://{}/http-api-resources", serviceId), ResponseBodyBean.class);
                            assert responseBodyBean != null;
                            val data = responseBodyBean.getData();
                            val httpApiResourcesResponse = mapper.convertValue(data, HttpApiResourcesResponse.class);
                            val serviceInfo = new GetServicesInfoResponse.ServiceInfo();
                            serviceInfo.setServiceId(serviceId);
                            serviceInfo.setHttpApiResources(httpApiResourcesResponse);
                            lazyDebug(log, () -> format("Added serviceInfo: {}", serviceInfo));
                            return serviceInfo;
                        }).collect(Collectors.toList())
        );
        if (CollUtil.isEmpty(response.getList())) {
            log.warn("Got am empty ServiceInfo list");
        }
        return response;
    }
}
