package com.jmsoftware.maf.authcenter.permission.service.impl;

import cn.hutool.core.util.StrUtil;
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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <h1>PermissionServiceImplTest</h1>
 * Description: PermissionServiceImplTest, change description here.
 * <p>
 * <h2>Mockito JUnit 5 Extension</h2>
 * <p>There is also a Mockito extension for JUnit 5 that will make the initialization even simpler.</p>
 * <p><strong>Pros:</strong></p>
 * <ul>
 * <li>No need to call <code>MockitoAnnotations.openMocks()</code></li>
 * <li>Validates framework usage and detects incorrect stubbing</li>
 * <li>Easy to create mocks</li>
 * <li>Very readable</li>
 *
 * </ul>
 * <p><strong>Cons:</strong></p>
 * <ul>
 * <li>Need an extra dependency on <code>org.mockito:mockito-junit-jupiter</code>, which has been included by Spring.
 * So we don&#39;t have to worry about this.</li>
 * </ul>
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 4/3/22 10:19 PM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito With JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito With JUnit 5</a>
 **/
@Slf4j
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
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
        log.info("{} setUp", this.getClass().getSimpleName());
    }

    @AfterEach
    void tearDown() {
        log.info("{} tearDown", this.getClass().getSimpleName());
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
        assertTrue(
                servicesInfo.getList()
                        .stream()
                        .anyMatch(service -> StrUtil.equalsAnyIgnoreCase(service.getServiceId(), "auth-center"))
        );
    }
}
