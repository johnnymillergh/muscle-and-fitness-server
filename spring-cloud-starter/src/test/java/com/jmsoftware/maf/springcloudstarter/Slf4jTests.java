package com.jmsoftware.maf.springcloudstarter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.jmsoftware.maf.springcloudstarter.function.Slf4j.lazyDebug;

/**
 * Description: Slf4jTests, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/19/2022 9:16 AM
 **/
@Slf4j
class Slf4jTests {
    @Test
    void lazyDebugTest() {
        Assertions.assertDoesNotThrow(() -> lazyDebug(log, () -> "Hello World! From lazyDebug test."));
    }
}
