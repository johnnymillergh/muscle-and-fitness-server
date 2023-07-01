package com.jmsoftware.maf.common.function

import cn.hutool.core.util.BooleanUtil
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * Require true or else throw exception.
 *
 * @param aBoolean the aBoolean
 * @param after    the after
 * @return the throw exception function
 */
fun requireTrue(aBoolean: Boolean, after: Consumer<Boolean>?): OrElseThrowExceptionFunction {
    after?.accept(aBoolean)
    return OrElseThrowExceptionFunction { exceptionSupplier: Supplier<Throwable> ->
        if (BooleanUtil.isFalse(aBoolean)) {
            throw exceptionSupplier.get()
        }
    }
}

/**
 * Require all true or else throw exception function.
 *
 * @param booleans the booleans
 * @return the or else throw exception function
 */
fun requireAllTrue(vararg booleans: Boolean): OrElseThrowExceptionFunction {
    return OrElseThrowExceptionFunction { exceptionSupplier: Supplier<Throwable> ->
        for (aBoolean in booleans) {
            if (BooleanUtil.isFalse(aBoolean)) {
                throw exceptionSupplier.get()
            }
        }
    }
}
