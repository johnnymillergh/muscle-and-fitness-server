package com.jmsoftware.maf.authcenter.role.service.impl;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jmsoftware.maf.authcenter.role.RoleExcelBean;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.common.exception.InternalServerException;
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
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
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * <h1>RoleDomainServiceImplTest</h1>
 * Description: RoleDomainServiceImplTest, change description here.
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
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 4/4/2022 8:40 AM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito With JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito With JUnit 5</a>
 **/
@Slf4j
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class RoleDomainServiceImplTest {
    @InjectMocks
    private RoleDomainServiceImpl roleDomainService;
    @Mock
    @SuppressWarnings("unused")
    private RoleMapper roleMapper;
    @Mock
    @SuppressWarnings("unused")
    private MafProjectProperties mafProjectProperties;
    @Mock
    @SuppressWarnings("unused")
    private MafConfigurationProperties mafConfigurationProperties;
    @Mock
    @SuppressWarnings("unused")
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    @SuppressWarnings("unused")
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        log.info("{} setUp", this.getClass().getSimpleName());
    }

    @AfterEach
    void tearDown() {
        log.info("{} tearDown", this.getClass().getSimpleName());
    }

    @Test
    void getRoleList() {
        when(this.redisTemplate.hasKey(anyString())).thenReturn(false);
        assertThrows(NullPointerException.class, () -> this.roleDomainService.getRoleList(1L));
    }

    @Test
    void getRoleListByUserId() {
        assertThrows(NullPointerException.class, () -> this.roleDomainService.getRoleListByUserId(1L));
    }

    @Test
    void checkAdmin() {
        assertThrows(MybatisPlusException.class, () -> this.roleDomainService.checkAdmin(Lists.newArrayList(1L)));
    }

    @Test
    void getListForExporting() {
        assertThrows(NullPointerException.class, () -> this.roleDomainService.getListForExporting());
    }

    @Test
    void validateBeforeAddToBeanList() {
        val roleExcelBean = new RoleExcelBean();
        assertThrows(
                IllegalArgumentException.class,
                () -> this.roleDomainService.validateBeforeAddToBeanList(
                        Lists.newArrayList(roleExcelBean),
                        roleExcelBean,
                        0
                )
        );
    }

    @Test
    void save() {
        assertThrows(InternalServerException.class, () -> this.roleDomainService.save(Lists.newArrayList()));
    }
}
