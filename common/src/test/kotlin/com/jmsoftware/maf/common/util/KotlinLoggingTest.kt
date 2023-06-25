package com.jmsoftware.maf.common.util

import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.util.StopWatch

/**
 * # KotlinLoggingTest
 *
 * Description: KotlinLoggingTest, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 6/23/2023 7:35 PM
 */
@Slf4j
@ExtendWith(MockitoExtension::class)
class KotlinLoggingTest {
    companion object {
        const val TESTING_TIMES = 50
        private val logger = logger()
    }

    @Test
    fun performanceTestForLogger() {
        assertDoesNotThrow {
            val stopWatchForLogger = StopWatch("Performance Testing for ${this.javaClass.simpleName}'s logger()")
            for (i in 1..TESTING_TIMES) {
                stopWatchForLogger.start("Loop #$i")
                logger.info("Performance Testing for logger(), $i")
                stopWatchForLogger.stop()
            }
            logger.info(stopWatchForLogger.prettyPrint())
        }
    }

    @Test
    fun performanceTestForSlf4j() {
        val logRef = log
        assertDoesNotThrow {
            val stopWatchForSlf4j = StopWatch("Performance Testing for ${this.javaClass.simpleName}'s @Slf4j")
            for (i in 1..TESTING_TIMES) {
                stopWatchForSlf4j.start("Loop #$i")
                log.info("Performance Testing for @Slf4j, $i")
                stopWatchForSlf4j.stop()
            }
            log.info(stopWatchForSlf4j.prettyPrint())
        }
        assertEquals(logRef.hashCode(), log.hashCode())
    }

    @Test
    fun fluentLogging() {
        assertDoesNotThrow {
            log.atDebug().log { "Hello World! From fluent-logging for Level.DEBUG level." }
            log.atInfo().log { "Hello World! From fluent-logging for Level.INFO level." }
        }
    }

    @Test
    fun testSlf4jAnnotation_whenAnnotatingInternalObject() {
        val result = assertDoesNotThrow { function1(1) }
        assertEquals(1, result)
    }

    @Test
    fun testSlf4jAnnotation_whenNotAnnotatingInternalObject() {
        assertDoesNotThrow { function2() }
    }
}
