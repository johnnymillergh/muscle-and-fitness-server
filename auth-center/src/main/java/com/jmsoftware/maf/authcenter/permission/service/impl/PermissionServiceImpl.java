package com.jmsoftware.maf.authcenter.permission.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.authcenter.permission.configuration.PermissionConfiguration;
import com.jmsoftware.maf.authcenter.permission.entity.GetServicesInfoResponse;
import com.jmsoftware.maf.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.maf.authcenter.permission.mapper.PermissionMapper;
import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse;
import com.jmsoftware.maf.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
    private final RoleService roleService;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final PermissionConfiguration permissionConfiguration;

    @Override
    public GetPermissionListByRoleIdListResponse getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload) {
        val adminRole = roleService.checkAdmin(payload.getRoleIdList());
        val response = new GetPermissionListByRoleIdListResponse();
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
        val permissionList = this.getPermissionListByRoleIdList(payload.getRoleIdList(),
                                                                payload.getPermissionTypeList());
        permissionList.forEach(permissionPersistence -> {
            val permission = new GetPermissionListByRoleIdListResponse.Permission();
            BeanUtil.copyProperties(permissionPersistence, permission);
            response.getPermissionList().add(permission);
        });
        return response;
    }

    @Override
    public List<PermissionPersistence> getPermissionListByRoleIdList(@NotEmpty List<Long> roleIdList,
                                                                     @NotEmpty List<PermissionType> permissionTypeList) {
        return this.getBaseMapper().selectPermissionListByRoleIdList(roleIdList, permissionTypeList);
    }

    @Override
    public GetServicesInfoResponse getServicesInfo() throws BusinessException {
        val serviceIdList = discoveryClient.getServices();
        log.info("Getting service info from Service ID list: {}", serviceIdList);
        val response = new GetServicesInfoResponse();
        val mapper = new ObjectMapper();
        log.info("Ignored service ID: {}", permissionConfiguration.getIgnoredServiceIds());
        for (String serviceId : serviceIdList) {
            if (CollUtil.contains(permissionConfiguration.getIgnoredServiceIds(), serviceId)) {
                log.warn("Ignored service ID: {}", serviceId);
                continue;
            }
            ResponseBodyBean<?> responseBodyBean = Optional.ofNullable(restTemplate.getForObject(
                    String.format("http://%s/http-api-resources", serviceId), ResponseBodyBean.class))
                    .orElseThrow(() -> new BusinessException("Internal service mustn't respond null"));
            val data = Optional.of(responseBodyBean.getData())
                    .orElseThrow(() -> new BusinessException("HttpApiResourcesResponse mustn't be null"));
            val httpApiResourcesResponse = mapper.convertValue(data, HttpApiResourcesResponse.class);
            val serviceInfo = new GetServicesInfoResponse.ServiceInfo();
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
