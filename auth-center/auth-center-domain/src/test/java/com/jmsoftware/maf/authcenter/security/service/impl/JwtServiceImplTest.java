package com.jmsoftware.maf.authcenter.security.service.impl;

import com.google.common.collect.Lists;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.springcloudstarter.property.JwtConfigurationProperties;
import io.jsonwebtoken.Claims;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * <h1>JwtServiceImplTest</h1>
 * Description: JwtServiceImplTest, change description here.
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
class JwtServiceImplTest {
    private final static long USER_ID = 1L;
    private final static String USERNAME = "ijohnnymiller";
    private final static String JWT = "eyJhbGciOiJIUzM4NCJ9" +
            ".eyJqdGkiOiIxIiwic3ViIjoiaWpvaG5ueW1pbGxlciIsImlhdCI6MTY0OTA0NTgzMiwicm9sZXMiOltdfQ" +
            ".lMQDOTnNsZTaopDBf7O_4zlhPJTZmGB3v6MpjmWnPuMEb2Aj-bXciDh4m2pAYtEp";
    @Spy
    @InjectMocks
    private JwtServiceImpl jwtService;
    @Mock
    private JwtConfigurationProperties jwtConfigurationProperties;
    @Mock
    private RedisTemplate<Object, Object> redisTemplate;
    @Mock
    private ValueOperations<Object, Object> valueOperations;

    @BeforeEach
    void setUp() {
        log.info("{} setUp", this.getClass().getSimpleName());
        when(this.jwtConfigurationProperties.getSigningKey())
                .thenReturn("signing-key-for-unit-test-only-do-not-use-in-production");
        this.jwtService.init();
    }

    @AfterEach
    void tearDown() {
        log.info("{} tearDown", this.getClass().getSimpleName());
    }

    @Test
    void createJwt() {
        when(this.redisTemplate.opsForValue()).thenReturn(this.valueOperations);
        doNothing().when(this.valueOperations).set(anyString(), anyString(), anyLong(), any());
        val jwt = this.jwtService.createJwt(true, USER_ID, USERNAME, Lists.newArrayList(), Lists.newArrayList());
        assertNotEquals(0, jwt.length());
    }

    @Test
    void parseJwt() {
        when(this.redisTemplate.opsForValue()).thenReturn(this.valueOperations);
        when(this.valueOperations.getOperations()).thenReturn(this.redisTemplate);
        val claimsAtomicReference = new AtomicReference<Claims>();
        assertThrows(SecurityException.class, () -> claimsAtomicReference.set(this.jwtService.parseJwt(JWT)));
        log.info("Parse JWT. claimsAtomicReference: {}", claimsAtomicReference.hashCode());
        assertNotNull(claimsAtomicReference);
        assertNull(claimsAtomicReference.get());
    }

    @Test
    void invalidateJwt() {
        when(this.redisTemplate.opsForValue()).thenReturn(this.valueOperations);
        when(this.valueOperations.getOperations()).thenReturn(this.redisTemplate);
        doReturn(JWT).when(this.jwtService).getJwtFromRequest(any());
        assertThrows(SecurityException.class, () -> this.jwtService.invalidateJwt(any()));
    }

    @Test
    void getUsernameFromJwt() {
        when(this.redisTemplate.opsForValue()).thenReturn(this.valueOperations);
        when(this.valueOperations.getOperations()).thenReturn(this.redisTemplate);
        assertThrows(SecurityException.class, () -> this.jwtService.getUsernameFromJwt(JWT));
    }

    @Test
    void getUsernameFromRequest() {
        when(this.redisTemplate.opsForValue()).thenReturn(this.valueOperations);
        when(this.valueOperations.getOperations()).thenReturn(this.redisTemplate);
        doReturn(JWT).when(this.jwtService).getJwtFromRequest(any());
        assertThrows(SecurityException.class, () -> this.jwtService.getUsernameFromRequest(any()));
    }

    @Test
    void getJwtFromRequest() {
        assertThrows(NullPointerException.class, () -> this.jwtService.getJwtFromRequest(any()));
    }

    @Test
    void parse() {
        assertThrows(NullPointerException.class, () -> this.jwtService.parse(any()));
    }
}
