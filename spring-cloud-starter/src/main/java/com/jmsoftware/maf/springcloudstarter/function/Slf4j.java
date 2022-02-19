/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
package com.jmsoftware.maf.springcloudstarter.function;

import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * Description: Slf4j, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/19/2022 9:04 AM
 **/
public class Slf4j {
    private Slf4j() {
    }

    /**
     * Lazy debug.
     *
     * @param logger   the logger
     * @param supplier the string supplier
     * @see com.jmsoftware.maf.springcloudstarter.Slf4jTests#lazyDebugTest()
     */
    public static void lazyDebug(Logger logger, Supplier<String> supplier) {
        if (logger.isDebugEnabled()) {
            logger.debug(supplier.get());
        }
    }
}
