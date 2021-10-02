package com.jmsoftware.maf.springcloudstarter;

import cn.hutool.core.util.StrUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionFunction;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionRunnable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.jmsoftware.maf.springcloudstarter.function.BooleanCheck.requireAllTrue;
import static com.jmsoftware.maf.springcloudstarter.function.BooleanCheck.requireTrue;
import static com.jmsoftware.maf.springcloudstarter.function.Cache.cacheFunction;
import static com.jmsoftware.maf.springcloudstarter.function.ExceptionHandling.computeAndHandleException;
import static com.jmsoftware.maf.springcloudstarter.function.ExceptionReturnDefault.computeOrGetDefault;
import static com.jmsoftware.maf.springcloudstarter.function.FunctionLog.logFunction;
import static com.jmsoftware.maf.springcloudstarter.function.Retry.retryFunction;
import static fj.Show.anyShow;

/**
 * FunctionalInterfaceTests
 *
 * @author @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/28/2021 12:46 PM
 * @version 1.0
 * @since <pre>Sep 28, 2021</pre>
 */
@Slf4j
class FunctionalInterfaceTests {
    /**
     * Test require true.
     */
    @Test
    @SuppressWarnings("ConstantConditions")
    void testRequireTrue() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> requireTrue(1 != 1, anotherBoolean -> log.info("aBoolean = {}", anotherBoolean))
                        .orElseThrow(() -> new IllegalArgumentException("aBoolean is expected to be true")),
                "requireTrue doesn't throws exception"
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> requireAllTrue(1 != 1, 2 == 2, 3 == 3)
                        .orElseThrow(() -> new IllegalArgumentException("Boolean array is expected to be all true")),
                "requireTrue doesn't throws exception"
        );
    }

    /**
     * Test cache function.
     */
    @Test
    @SneakyThrows
    void testCacheFunction() {
        val cacheMap = Maps.<String, String>newHashMap();
        cacheMap.put("key1", "1");
        cacheMap.put("key2", "2");
        final Function<String, String> stringProcess = input -> {
            log.info("No cache return value found. input: {} Re-calculating…", input);
            return StrUtil.subSuf(input, 3);
        };
        val result1 = cacheFunction(stringProcess, "key1", cacheMap);
        val result2 = cacheFunction(stringProcess, "key2", cacheMap);
        val result3 = cacheFunction(stringProcess, "key3", cacheMap);
        Assertions.assertEquals("1", result1);
        Assertions.assertEquals("2", result2);
        Assertions.assertEquals("3", result3);
        Assertions.assertEquals(3, cacheMap.size());

        val guavaCache = CacheBuilder
                .newBuilder()
                .maximumSize(2)
                .expireAfterWrite(Duration.ofSeconds(3))
                .<String, String>removalListener(notification -> log.info("Removed cache: {}", notification))
                .build();
        guavaCache.put("key4", "4");
        guavaCache.put("key5", "5");
        val result4 = cacheFunction(stringProcess, "key4", guavaCache);
        val result5 = cacheFunction(stringProcess, "key5", guavaCache);
        val result6 = cacheFunction(stringProcess, "key6", guavaCache);
        anyShow().print("Before cache invalidated: ");
        anyShow().println(guavaCache.asMap().entrySet());
        Assertions.assertEquals("4", result4);
        Assertions.assertEquals("5", result5);
        Assertions.assertEquals("6", result6);
        Thread.sleep(Duration.ofSeconds(4).toMillis());
        Assertions.assertNull(guavaCache.getIfPresent("key5"), "The value of Guava cache map is supposed to be null");
        anyShow().print("After cache invalidated: ");
        anyShow().println(guavaCache.asMap().entrySet());
    }

    @Test
    void testComputeAndHandleException() {
        final ThrowExceptionFunction<Integer, Double> compute = value -> (double) (1 / value);
        final Function<Exception, Double> handleException = e -> {
            log.warn("Exception found. {}. Returning default value…", e.getMessage());
            return 0D;
        };
        val result1 = computeAndHandleException(compute, 1, handleException);
        val result2 = computeAndHandleException(compute, 0, handleException);
        log.info("result1 = {}, result2 = {}", result1, result2);
        Assertions.assertEquals(0D, result2);
    }

    @Test
    void testComputeOrGetDefault() {
        final ThrowExceptionFunction<Integer, Double> compute = value -> (double) (1 / value);
        val result = computeOrGetDefault(compute, null, 0D);
        log.info("computeOrGetDefault result = {}", result);
        Assertions.assertEquals(0D, result);
    }

    @Test
    void testLogFunction() {
        final Function<Integer, String> compute = value -> {
            try {
                Thread.sleep(Duration.ofSeconds(value).toMillis());
            } catch (InterruptedException ignored) {
            }
            return "Hello, world!";
        };
        logFunction(compute, 2, this.getClass().getSimpleName());
    }

    @Test
    void tesRetryFunction() {
        val counter = new AtomicInteger();
        final ThrowExceptionRunnable compute = () -> {
            log.info("Function `compute` called at {} time(s)", (counter.get() + 1));
            counter.getAndIncrement();
            // Somehow, the code throws an exception
            throw new IllegalStateException("Oops! Somehow, the code throws an exception");
        };
        Assertions.assertThrows(RuntimeException.class, () -> retryFunction(compute, 5));
    }
} 
