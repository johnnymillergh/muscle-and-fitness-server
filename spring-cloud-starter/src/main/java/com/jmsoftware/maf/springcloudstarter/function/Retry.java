package com.jmsoftware.maf.springcloudstarter.function;

import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionBiFunction;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionFunction;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionRunnable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <h1>Retry</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:27 AM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-1'>Java 函数式编程最佳实践 - 赋予方法重试能力</a>
 **/
public class Retry {
    private Retry() {
    }

    /**
     * Retry function.
     *
     * @param runnable  the runnable
     * @param retryTime the retry time
     * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests#tesRetryFunction()
     */
    public static void retryFunction(ThrowExceptionRunnable runnable, int retryTime) {
        while (true) {
            try {
                runnable.run();
                return;
            } catch (Exception e) {
                retryTime--;
                if (retryTime <= 0) {
                    throw new RuntimeException(e);
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
     */
    public static <T, R> R retryFunction(ThrowExceptionFunction<T, R> function, T t, int retryTime) {
        while (true) {
            try {
                return function.apply(t);
            } catch (Exception e) {
                retryTime--;
                if (retryTime <= 0) {
                    throw new RuntimeException(e);
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
     * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests#tesRetryFunction()
     */
    public static <T, R> R retryFunction(Function<T, R> function, T t, Predicate<R> predicator, int retryTime
    ) throws IllegalStateException {
        while (retryTime > 0) {
            try {
                final var r = function.apply(t);
                if (predicator.test(r)) {
                    return r;
                }
            } finally {
                retryTime--;
            }
        }
        throw new IllegalStateException("retryTime reached hits the limit and the result is not correct");
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
     */
    public static <T, U, R> R retryFunction(ThrowExceptionBiFunction<T, U, R> function, T t, U u, int time) {
        while (true) {
            try {
                return function.apply(t, u);
            } catch (Exception e) {
                time--;
                if (time <= 0) {
                    throw new RuntimeException(e);
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
    public static <T, U, R> R retryFunction(BiFunction<T, U, R> function, T t, U u, Predicate<R> predicator,
                                            int retryTime
    ) throws IllegalStateException {
        while (retryTime > 0) {
            try {
                final var r = function.apply(t, u);
                if (predicator.test(r)) {
                    return r;
                }
            } finally {
                retryTime--;
            }
        }
        throw new IllegalStateException("retryTime reached hits the limit and the result is not correct");
    }
}
