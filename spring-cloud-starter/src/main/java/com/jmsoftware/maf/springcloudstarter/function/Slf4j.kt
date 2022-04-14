package com.jmsoftware.maf.springcloudstarter.function

import org.slf4j.Logger
import java.util.function.Supplier

/**
 * Lazy debug.
 *
 * @param logger   the logger
 * @param supplier the string supplier
 * @see com.jmsoftware.maf.springcloudstarter.Slf4jTests.lazyDebugTest
 */
fun lazyDebug(logger: Logger, supplier: Supplier<String>) {
    if (logger.isDebugEnabled) {
        logger.debug(supplier.get())
    }
}
