package com.jmsoftware.maf.common.function

import cn.hutool.core.util.StrUtil
import com.google.common.cache.CacheBuilder
import com.google.common.cache.RemovalNotification
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.bean.PaginationBase
import com.jmsoftware.maf.common.util.Slf4j
import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function
import java.util.function.Predicate

/**
 * # FunctionTests
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/28/2021 12:46 PM
 */
@Slf4j
class FunctionTests {
    /**
     * Test require true.
     */
    @Test
    fun testRequireTrue() {
        assertThrows(
            IllegalArgumentException::class.java, {
                requireTrue(false) { anotherBoolean: Boolean? -> log.info("aBoolean = $anotherBoolean") }
                    .orElseThrow { IllegalArgumentException("aBoolean is expected to be true") }
            },
            "requireTrue doesn't throws exception"
        )
        assertThrows(
            IllegalArgumentException::class.java, {
                requireAllTrue(false, true, true)
                    .orElseThrow { IllegalArgumentException("Boolean array is expected to be all true") }
            },
            "requireTrue doesn't throws exception"
        )
    }

    /**
     * Test cache function.
     */
    @Test
    fun testCacheFunction() {
        val cacheMap = mutableMapOf("key1" to "1", "key2" to "2")
        val stringProcess = Function { input: String ->
            log.info("No cache return value found. input: $input Re-calculating…")
            StrUtil.subSuf(input, 3)
        }
        val result1 = cacheFunction(stringProcess, "key1", cacheMap)
        val result2 = cacheFunction(stringProcess, "key2", cacheMap)
        val result3 = cacheFunction(stringProcess, "key3", cacheMap)
        assertEquals("1", result1)
        assertEquals("2", result2)
        assertEquals("3", result3)
        assertEquals(3, cacheMap.size)
        val guavaCache = CacheBuilder
            .newBuilder()
            .maximumSize(2)
            .expireAfterWrite(Duration.ofSeconds(3))
            .removalListener { notification: RemovalNotification<String?, String?>? ->
                log.info("Removed cache: $notification")
            }.build<String, String>()
        guavaCache.put("key4", "4")
        guavaCache.put("key5", "5")
        val result4 = cacheFunction(stringProcess, "key4", guavaCache)
        val result5 = cacheFunction(stringProcess, "key5", guavaCache)
        val result6 = cacheFunction(stringProcess, "key6", guavaCache)
        assertEquals("4", result4)
        assertEquals("5", result5)
        assertEquals("6", result6)
        Thread.sleep(Duration.ofSeconds(4).toMillis())
        assertNull(guavaCache.getIfPresent("key5"), "The value of Guava cache map is supposed to be null")
    }

    @Test
    fun testComputeAndHandleException() {
        val compute = ThrowExceptionFunction { value: Int -> (1 / value).toDouble() }
        val handleException = Function { e: Exception ->
            log.warn("Exception found. ${e.message}. Returning default value…")
            0.0
        }
        val result1 = computeAndHandleException(compute, 1, handleException)
        val result2 = computeAndHandleException(compute, 0, handleException)
        log.info("result1 = $result1, result2 = $result2")
        assertEquals(0.0, result2)
    }

    @Test
    fun testComputeOrGetDefault() {
        val compute = ThrowExceptionFunction { value: Int? -> (1 / value!!).toDouble() }
        val result = computeOrGetDefault(compute, null, 0.0)
        log.info("computeOrGetDefault result = $result")
        assertEquals(0.0, result)
    }

    @Test
    fun testLogFunction() {
        val compute = Function { value: Int ->
            try {
                Thread.sleep(Duration.ofSeconds(value.toLong()).toMillis())
            } catch (ignored: InterruptedException) {
            }
            "Hello, world!"
        }
        logFunction(compute, 2, this.javaClass.simpleName)
    }

    @Test
    fun tesRetryFunction() {
        val counter = AtomicInteger()
        val compute = ThrowExceptionRunnable {
            log.info("Function `compute` called at ${counter.get() + 1} time(s)")
            counter.getAndIncrement()
            throw IllegalStateException("Oops! Somehow, the code throws an exception")
        }
        assertThrows(RuntimeException::class.java) { retryFunction(compute, 5) }
        val compute2 = Function { value: Int -> 1 + value }
        val result = retryFunction(compute2, -1, { integer: Int -> integer == 0 }, 3)
        assertEquals(0, result)
        assertThrows(
            IllegalStateException::class.java
        ) { retryFunction(compute2, 1, Predicate { integer: Int -> integer == 0 }, 3) }
    }

    @Test
    fun testPaginationResponseComposer_whenPageSizeIs10_andTotalIs100() {
        val paginationBase = PaginationBase().apply {
            currentPage = 1
            pageSize = 10
        }
        val total = 100L
        val function = Function { base: PaginationBase ->
            log.info("Getting page results for pagination: $base")
            PageResponseBodyBean.ofSuccess(
                (1..10).toList(),
                total
            )
        }
        val result = composePages(paginationBase = paginationBase, callback = function)
        assertEquals(10, paginationBase.currentPage)
        assertEquals(total.toInt(), result.size)
    }
}
