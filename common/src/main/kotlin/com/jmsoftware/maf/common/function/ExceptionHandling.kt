@file:Suppress("unused")

package com.jmsoftware.maf.common.function

import java.util.function.Function

/**
 * # ExceptionHandling
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:20 PM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-4'>Java 函数式编程最佳实践 - 赋予函数处理异常的能力</a>
 */
private object ExceptionHandling

/**
 * Compute and deal exception r.
 *
 * @param <T>          the type parameter
 * @param <R>          the type parameter
 * @param function     the function
 * @param t            the t
 * @param handlingFunc the handling func
 * @return the r
 */
fun <T, R> computeAndHandleException(
    function: ThrowExceptionFunction<T, R>,
    t: T,
    handlingFunc: Function<Exception, R>
): R {
    return try {
        function.apply(t)
    } catch (e: Exception) {
        handlingFunc.apply(e)
    }
}

/**
 * Compute and handle exception r.
 *
 * @param <T>          the type parameter
 * @param <U>          the type parameter
 * @param <R>          the type parameter
 * @param function     the function
 * @param t            the t
 * @param u            the u
 * @param handlingFunc the handling func
 * @return the r
</R></U></T> */
fun <T, U, R> computeAndHandleException(
    function: ThrowExceptionBiFunction<T, U, R>,
    t: T,
    u: U,
    handlingFunc: Function<Exception, R>
): R {
    return try {
        function.apply(t, u)
    } catch (e: Exception) {
        handlingFunc.apply(e)
    }
}

/**
 * Compute and handle exception r.
 *
 * @param <R>          the type parameter
 * @param supplier     the supplier
 * @param handlingFunc the handling func
 * @return the r
</R> */
fun <R> computeAndHandleException(
    supplier: ThrowExceptionSupplier<R>,
    handlingFunc: Function<Exception, R>
): R {
    return try {
        supplier.get()
    } catch (e: Exception) {
        handlingFunc.apply(e)
    }
}
