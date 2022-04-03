package com.jmsoftware.maf.authcenter.permission.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jmsoftware.maf.authcenter.permission.configuration.PermissionConfiguration;
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService;
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.springbootstarter.HttpApiResourcesResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Description: PermissionServiceImplTest, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 4/3/22 10:19 PM
 **/
@Slf4j
@ExtendWith(MockitoExtension.class)
class PermissionServiceImplTest {
    @InjectMocks
    private PermissionServiceImpl permissionService;
    @Mock
    private PermissionDomainService permissionDomainService;
    @Mock
    private RoleDomainService roleDomainService;
    @Mock
    private DiscoveryClient discoveryClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PermissionConfiguration permissionConfiguration;

    @BeforeEach
    void setUp() {
        log.info("PermissionServiceImplTest setUp");
    }

    @AfterEach
    void tearDown() {
        log.info("PermissionServiceImplTest tearDown");
    }

    @Test
    void getPermissionListByRoleIdList() {
        when(this.roleDomainService.checkAdmin(anyList())).thenReturn(false);
        when(this.permissionDomainService.getPermissionListByRoleIdList(anyList(), anyList()))
                .thenReturn(Lists.newArrayList());

        val payload = new GetPermissionListByRoleIdListPayload();
        payload.setRoleIdList(Lists.newArrayList());
        payload.setPermissionTypeList(Lists.newArrayList());
        val response = this.permissionService.getPermissionListByRoleIdList(payload);
        log.info("Permission list response: {}", response);
        verify(this.roleDomainService).checkAdmin(anyList());
        verify(this.permissionDomainService).getPermissionListByRoleIdList(anyList(), anyList());
        assertEquals(0, response.getPermissionList().size());
    }

    @Test
    void getServicesInfo() {
        when(this.discoveryClient.getServices())
                .thenReturn(Lists.newArrayList(
                        "auth-center", "oss-center", "maf-mis", "api-gateway", "spring-boot-admin"));
        when(this.permissionConfiguration.getIgnoredServiceIds())
                .thenReturn(Sets.newHashSet("api-gateway", "spring-boot-admin"));
        when(this.restTemplate.getForObject(anyString(), any()))
                .thenReturn(ResponseBodyBean.ofSuccess(new HttpApiResourcesResponse()));

        val servicesInfo = this.permissionService.getServicesInfo();
        log.info("Services info: {}", servicesInfo);
        verify(this.discoveryClient).getServices();
        assertNotEquals(0, servicesInfo.getList().size());
    }
}
