package com.jmsoftware.maf.springcloudstarter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static com.jmsoftware.maf.springcloudstarter.function.Slf4jKt.lazyDebug;

/**
 * Description: Slf4jTests, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 3/23/22 8:01 AM
 **/
@Slf4j
@Execution(ExecutionMode.CONCURRENT)
class Slf4jTests {
    @Test
    void lazyDebugTest() {
        Assertions.assertDoesNotThrow(() -> lazyDebug(log, () -> "Hello World! From lazyDebug test."));
    }
}
