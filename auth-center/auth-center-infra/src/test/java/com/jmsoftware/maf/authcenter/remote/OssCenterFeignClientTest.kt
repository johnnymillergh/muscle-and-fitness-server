package com.jmsoftware.maf.authcenter.remote

import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockMultipartFile

/**
 * # OssCenterFeignClientTest
 *
 * Description: PermissionDomainServiceImplTest, change description here.
 *
 * ## Mockito JUnit 5 Extension
 *
 * There is also a Mockito extension for JUnit 5 that will make the initialization even simpler.
 *
 * **Pros:**
 *
 *  * No need to call `MockitoAnnotations.openMocks()`
 *  * Validates framework usage and detects incorrect stubbing
 *  * Easy to create mocks
 *  * Very readable
 *
 * **Cons:**
 *
 *  * Need an extra dependency on `org.mockito:mockito-junit-jupiter`, which has been included by Spring.
 * So we don&#39;t have to worry about this.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:43 AM
 * @see <a href='https://www.arhohuttunen.com/junit-5-mockito/'>Using Mockito With JUnit 5</a>
 * @see <a href='https://www.youtube.com/watch?v=p7_cTAF39A8/'>YouTube - Using Mockito With JUnit 5</a>
 */
@ExtendWith(MockitoExtension::class)
@Execution(ExecutionMode.CONCURRENT)
internal class OssCenterFeignClientTest {
    companion object {
        private val log = logger()
    }

    @Mock
    private lateinit var ossCenterFeignClient: OssCenterFeignClient

    @Mock
    private lateinit var ossCenterFeignClientFallback: OssCenterFeignClient.OssCenterFeignClientFallback

    @BeforeEach
    fun setUp() {
        log.info("${this.javaClass.simpleName} setUp")
    }

    @AfterEach
    fun tearDown() {
        log.info("${this.javaClass.simpleName} tearDown")
    }

    @Test
    fun uploadSingleResource() {
        val multipartFile = MockMultipartFile(
            "name-for-unit-test.txt",
            "original-filename.txt",
            null,
            null
        )
        val response = ossCenterFeignClient.uploadSingleResource(multipartFile)
        log.info("Response: $response")
        assertNull(response)
    }
}
