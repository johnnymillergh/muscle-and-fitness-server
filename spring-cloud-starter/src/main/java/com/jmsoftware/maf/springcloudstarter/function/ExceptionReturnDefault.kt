@file:Suppress("unused")

package com.jmsoftware.maf.springcloudstarter.function

/**
 * # ExceptionReturnDefault
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 2:52 PM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-3'>Java 函数式编程最佳实践 - 赋予函数报错返回默认值能力</a>
 */
private object ExceptionReturnDefault

/**
 * Compute or get default r.
 *
 * @param <T>      the type parameter
 * @param <R>      the type parameter
 * @param function the function
 * @param t        the t
 * @param r        the r
 * @return the r
 * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests.testComputeOrGetDefault
</R></T> */
fun <T, R> computeOrGetDefault(function: ThrowExceptionFunction<T, R>, t: T, r: R): R {
    return try {
        function.apply(t)
    } catch (e: Exception) {
        r
    }
}

/**
 * Compute or get default r.
 *
 * @param <R>      the type parameter
 * @param supplier the supplier
 * @param r        the r
 * @return the r
</R> */
fun <R> computeOrGetDefault(supplier: ThrowExceptionSupplier<R>, r: R): R {
    return try {
        supplier.get()
    } catch (e: Exception) {
        r
    }
}
