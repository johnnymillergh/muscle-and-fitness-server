package com.jmsoftware.maf.authcenter.user.service.impl;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.authcenter.user.mapper.UserRoleMapper;
import com.jmsoftware.maf.authcenter.user.persistence.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * <h1>UserRoleDomainServiceImplTest</h1>
 * Description: UserRoleDomainServiceImplTest, change description here.
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
class UserRoleDomainServiceImplTest {
    @InjectMocks
    private UserRoleDomainServiceImpl userRoleDomainService;
    @Mock
    private UserRoleMapper userRoleMapper;
    @Mock
    private RoleDomainService roleDomainService;

    @BeforeEach
    void setUp() {
        log.info("{} setUp", this.getClass().getSimpleName());
    }

    @AfterEach
    void tearDown() {
        log.info("{} tearDown", this.getClass().getSimpleName());
    }

    @Test
    void assignRoleByRoleName() {
        assertThrows(
                MybatisPlusException.class,
                () -> this.userRoleDomainService.assignRoleByRoleName(new User(), "ROLE_ADMIN_FOR_UNIT_TEST")
        );
    }
}
