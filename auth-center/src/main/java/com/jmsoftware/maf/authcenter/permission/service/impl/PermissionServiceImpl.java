package com.jmsoftware.maf.authcenter.permission.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jmsoftware.maf.authcenter.permission.entity.GetServicesInfoResponse;
import com.jmsoftware.maf.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.maf.authcenter.permission.mapper.PermissionMapper;
import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.authcenter.universal.configuration.ProjectProperty;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse;
import com.jmsoftware.maf.common.exception.BusinessException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <h1>PermissionServiceImpl</h1>
 * <p>
 * Service implementation of Permission.(Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionPersistence> implements PermissionService {
    private final DiscoveryClient discoveryClient;
    private final ProjectProperty projectProperty;
    private final RestTemplate restTemplate;

    @Override
    public GetPermissionListByRoleIdListResponse getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload) {
        val permissionList = this.getPermissionListByRoleIdList(payload.getRoleIdList());
        val response = new GetPermissionListByRoleIdListResponse();
        permissionList.forEach(permissionPersistence -> {
            GetPermissionListByRoleIdListResponse.Permission permission =
                    new GetPermissionListByRoleIdListResponse.Permission();
            BeanUtil.copyProperties(permissionPersistence, permission);
            response.getPermissionList().add(permission);
        });
        return response;
    }

    @Override
    public List<PermissionPersistence> getPermissionListByRoleIdList(@NonNull List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)) {
            return Collections.emptyList();
        }
        return this.getBaseMapper().selectPermissionListByRoleIdList(roleIdList);
    }

    @Override
    public GetPermissionListByUserIdResponse getPermissionListByUserId(@NotNull Long userId) {
        val permissionList = this.getPermissionPersistenceListByUserId(userId);
        val response = new GetPermissionListByUserIdResponse();
        permissionList.forEach(permissionPersistence -> {
            val permission = new GetPermissionListByUserIdResponse.Permission();
            BeanUtil.copyProperties(permissionPersistence, permission);
            response.getPermissionList().add(permission);
        });
        return response;
    }

    @Override
    public List<PermissionPersistence> getPermissionPersistenceListByUserId(@NotNull Long userId) {
        return this.getBaseMapper().selectPermissionListByUserId(userId);
    }

    @Override
    public GetServicesInfoResponse getServicesInfo() throws BusinessException {
        val serviceIdList = discoveryClient.getServices();
        log.info("Getting service info from Service ID list: {}", serviceIdList);
        val response = new GetServicesInfoResponse();
        val mapper = new ObjectMapper();
        val ignoredServiceIdList = Lists.newArrayList(projectProperty.getProjectArtifactId(),
                                                      "api-gateway", "spring-boot-admin");
        log.info("Ignored service ID list: {}", ignoredServiceIdList);
        for (String serviceId : serviceIdList) {
            if (ignoredServiceIdList.contains(serviceId)) {
                log.warn("Ignored service ID: {}", serviceId);
                continue;
            }
            ResponseBodyBean<?> responseBodyBean = Optional.ofNullable(restTemplate.getForObject(
                    String.format("http://%s/http-api-resources", serviceId), ResponseBodyBean.class))
                    .orElseThrow(() -> new BusinessException("Internal service mustn't respond null"));
            val data = Optional.of(responseBodyBean.getData())
                    .orElseThrow(() -> new BusinessException("HttpApiResourcesResponse mustn't be null"));
            HttpApiResourcesResponse httpApiResourcesResponse = mapper.convertValue(data,
                                                                                    HttpApiResourcesResponse.class);
            GetServicesInfoResponse.ServiceInfo serviceInfo = new GetServicesInfoResponse.ServiceInfo();
            serviceInfo.setServiceId(serviceId);
            serviceInfo.setHttpApiResources(httpApiResourcesResponse);
            response.getList().add(serviceInfo);
        }
        if (CollUtil.isEmpty(response.getList())) {
            log.warn("Got am empty ServiceInfo list");
        }
        return response;
    }
}
