@file:Suppress("unused")

package com.jmsoftware.maf.springcloudstarter.function

import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate

/**
 * # Retry
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 2:55 PM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-1'>Java 函数式编程最佳实践 - 赋予方法重试能力</a>
 */
private object Retry

/**
 * Retry function.
 *
 * @param runnable  the runnable
 * @param retryTime the retry time
 * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests.tesRetryFunction
 */
fun retryFunction(runnable: ThrowExceptionRunnable, retryTime: Int) {
    var retryTime1 = retryTime
    while (true) {
        try {
            runnable.run()
            return
        } catch (e: Exception) {
            retryTime1--
            if (retryTime1 <= 0) {
                throw RuntimeException(e)
            }
        }
    }
}

/**
 * Retry function r.
 *
 * @param <T>       the type parameter
 * @param <R>       the type parameter
 * @param function  the function
 * @param t         the t
 * @param retryTime the retry time
 * @return the r
</R></T> */
fun <T, R> retryFunction(function: ThrowExceptionFunction<T, R>, t: T, retryTime: Int): R {
    var retryTime1 = retryTime
    while (true) {
        try {
            return function.apply(t)
        } catch (e: Exception) {
            retryTime1--
            if (retryTime1 <= 0) {
                throw RuntimeException(e)
            }
        }
    }
}

/**
 * Retry function r.
 *
 * @param <T>        the type parameter
 * @param <R>        the type parameter
 * @param function   the function
 * @param t          the t
 * @param predicator the predicator
 * @param retryTime  the retry time
 * @return the r
 * @throws IllegalStateException the illegal state exception
 * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests.tesRetryFunction
</R></T> */
@Throws(IllegalStateException::class)
fun <T, R> retryFunction(
    function: Function<T, R>, t: T, predicator: Predicate<R>, retryTime: Int
): R {
    var retryTime1 = retryTime
    while (retryTime1 > 0) {
        try {
            val r = function.apply(t)
            if (predicator.test(r)) {
                return r
            }
        } finally {
            retryTime1--
        }
    }
    throw IllegalStateException("retryTime reached hits the limit and the result is not correct")
}

/**
 * Retry function r.
 *
 * @param <T>      the type parameter
 * @param <U>      the type parameter
 * @param <R>      the type parameter
 * @param function the function
 * @param t        the t
 * @param u        the u
 * @param time     the time
 * @return the r
</R></U></T> */
fun <T, U, R> retryFunction(function: ThrowExceptionBiFunction<T, U, R>, t: T, u: U, time: Int): R {
    var time1 = time
    while (true) {
        try {
            return function.apply(t, u)
        } catch (e: Exception) {
            time1--
            if (time1 <= 0) {
                throw RuntimeException(e)
            }
        }
    }
}

/**
 * Retry function r.
 *
 * @param <T>        the type parameter
 * @param <U>        the type parameter
 * @param <R>        the type parameter
 * @param function   the function
 * @param t          the t
 * @param u          the u
 * @param predicator the predicator
 * @param retryTime  the retry time
 * @return the r
 * @throws IllegalStateException the illegal state exception
 */
fun <T, U, R> retryFunction(
    function: BiFunction<T, U, R>, t: T, u: U, predicator: Predicate<R>,
    retryTime: Int
): R {
    var retryTime1 = retryTime
    while (retryTime1 > 0) {
        try {
            val r: R = function.apply(t, u)
            if (predicator.test(r)) {
                return r
            }
        } finally {
            retryTime1--
        }
    }
    throw IllegalStateException("retryTime reached hits the limit and the result is not correct")
}
