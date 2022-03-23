package com.jmsoftware.maf.springcloudstarter.function;

import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * Description: Slf4j, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 3/23/22 8:01 AM
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
