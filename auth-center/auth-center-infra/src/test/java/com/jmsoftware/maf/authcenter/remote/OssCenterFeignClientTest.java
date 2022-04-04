package com.jmsoftware.maf.authcenter.remote;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
class OssCenterFeignClientTest {
    @BeforeEach
    void setUp() {
        log.info("{} setUp", this.getClass().getSimpleName());
    }

    @AfterEach
    void tearDown() {
        log.info("{} tearDown", this.getClass().getSimpleName());
    }

    @Test
    void uploadSingleResource() {
    }
}
