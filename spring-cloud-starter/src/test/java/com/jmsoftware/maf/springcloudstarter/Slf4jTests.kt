package com.jmsoftware.maf.springcloudstarter

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.function.lazyDebug
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

/**
 * # Slf4jTests
 *
 * Description: Slf4jTests, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:29 AM
 */
@Execution(ExecutionMode.CONCURRENT)
internal class Slf4jTests {
    companion object {
        private val log = logger()
    }

    @Test
    fun lazyDebugTest() {
        Assertions.assertDoesNotThrow { lazyDebug(log) { "Hello World! From lazyDebug test." } }
    }
}
