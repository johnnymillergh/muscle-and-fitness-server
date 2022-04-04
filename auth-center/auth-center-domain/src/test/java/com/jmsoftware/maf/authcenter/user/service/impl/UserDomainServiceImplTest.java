package com.jmsoftware.maf.authcenter.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.authcenter.user.mapper.UserMapper;
import com.jmsoftware.maf.authcenter.user.payload.GetUserPageListPayload;
import com.jmsoftware.maf.authcenter.user.payload.GetUserStatusPayload;
import com.jmsoftware.maf.authcenter.user.service.UserRoleDomainService;
import com.jmsoftware.maf.common.domain.authcenter.user.LoginPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.SignupPayload;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import com.jmsoftware.maf.springcloudstarter.util.UserUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;

/**
 * <h1>UserDomainServiceImplTest</h1>
 * Description: UserDomainServiceImplTest, change description here.
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
class UserDomainServiceImplTest {
    private final static String USERNAME = "ijohnnymiller";
    @Spy
    @InjectMocks
    private UserDomainServiceImpl userDomainService;
    @Mock
    private UserMapper userMapper;
    @Mock
    @SuppressWarnings("unused")
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    @SuppressWarnings("unused")
    private JwtService jwtService;
    @Mock
    @SuppressWarnings("unused")
    private MessageSource messageSource;
    @Mock
    @SuppressWarnings("unused")
    private MafProjectProperties mafProjectProperties;
    @Mock
    @SuppressWarnings("unused")
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    @SuppressWarnings("unused")
    private UserRoleDomainService userRoleDomainService;
    @Mock
    @SuppressWarnings("unused")
    private MafConfigurationProperties mafConfigurationProperties;

    @BeforeEach
    void setUp() {
        log.info("{} setUp", this.getClass().getSimpleName());
    }

    @AfterEach
    void tearDown() {
        log.info("{} tearDown", this.getClass().getSimpleName());
    }

    @Test
    void getUserByLoginToken() {
        doReturn(this.userMapper).when(this.userDomainService).getBaseMapper();
        val user = this.userDomainService.getUserByLoginToken(USERNAME);
        log.info("User: {}", user);
        assertNull(user);
    }

    @Test
    void saveUserForSignup() {
        doReturn(this.userMapper).when(this.userDomainService).getBaseMapper();
        val signupResponse = this.userDomainService.saveUserForSignup(new SignupPayload());
        log.info("Signup response: {}", signupResponse);
        assertNotNull(signupResponse);
    }

    @Test
    void login() {
        doReturn(this.userMapper).when(this.userDomainService).getBaseMapper();
        assertThrows(SecurityException.class, () -> this.userDomainService.login(new LoginPayload()));
    }

    @Test
    @SneakyThrows
    void logout() {
        val logout = this.userDomainService.logout(any());
        assertTrue(logout);
    }

    @Test
    void getUserStatus() {
        @Cleanup val dummy = mockStatic(UserUtil.class);
        dummy.when(UserUtil::getCurrentUsername).thenReturn("ijohnnymiller");
        val payload = new GetUserStatusPayload();
        payload.setStatus((byte) 1);
        payload.setStatus2((byte) 0);
        val userStatus = this.userDomainService.getUserStatus(payload);
        log.info("User status: {}", userStatus);
        assertNotNull(userStatus);
        assertTrue(StrUtil.isNotBlank(userStatus));
    }

    @Test
    void getUserPageList() {
        doReturn(this.userMapper).when(this.userDomainService).getBaseMapper();
        val response = this.userDomainService.getUserPageList(new GetUserPageListPayload());
        log.info("User page list: {}", response);
        assertNotNull(response);
        assertTrue(CollUtil.isEmpty(response.getList()));
    }
}
