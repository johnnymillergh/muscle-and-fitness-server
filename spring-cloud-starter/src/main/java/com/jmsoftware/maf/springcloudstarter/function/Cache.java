package com.jmsoftware.maf.springcloudstarter.function;

import java.util.Map;
import java.util.function.Function;

/**
 * <h1>Cache</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:51 AM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-2'>Java 函数式编程最佳实践 - 赋予函数缓存能力</a>
 **/
public class Cache {
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
    public static <T, R> R cacheFunction(Function<T, R> function, T t, Map<T, R> cache) {
        R r = cache.get(t);
        if (r != null) {
            return r;
        }
        R result = function.apply(t);
        cache.put(t, result);
        return result;
    }
}
