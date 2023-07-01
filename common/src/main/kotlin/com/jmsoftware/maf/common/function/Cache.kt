@file:Suppress("unused")

package com.jmsoftware.maf.common.function

import com.google.common.cache.Cache
import java.util.function.Function

/**
 * # Cache
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 2:44 PM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-2'>Java 函数式编程最佳实践 - 赋予函数缓存能力</a>
 */
private object Cache

/**
 * Cache function r.
 *
 * @param <T>      the type parameter
 * @param <R>      the type parameter
 * @param function the function
 * @param t        the t
 * @param cache    the cache
 * @return the r
 */
fun <T, R> cacheFunction(function: Function<T, R>, t: T, cache: MutableMap<T, R>): R {
    val r = cache[t]
    if (r != null) {
        return r
    }
    val result = function.apply(t)
    cache[t] = result
    return result
}

/**
 * Cache function r.
 *
 * @param <T>      the type parameter
 * @param <R>      the type parameter
 * @param function the function
 * @param t        the t
 * @param cache    the cache
 * @return the r
 */
fun <T : Any, R : Any> cacheFunction(function: Function<T, R>, t: T, cache: Cache<T, R>): R {
    val r = cache.getIfPresent(t)
    if (r != null) {
        return r
    }
    val result = function.apply(t)
    cache.put(t, result)
    return result
}
