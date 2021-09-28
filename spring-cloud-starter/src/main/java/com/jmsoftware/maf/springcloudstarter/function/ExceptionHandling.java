package com.jmsoftware.maf.springcloudstarter.function;

import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionBiFunction;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionFunction;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionSupplier;

import java.util.function.Function;

/**
 * <h1>ExceptionHandling</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:54 AM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-4'>Java 函数式编程最佳实践 - 赋予函数处理异常的能力</a>
 **/
public class ExceptionHandling {
    /**
     * Compute and deal exception r.
     *
     * @param <T>          the type parameter
     * @param <R>          the type parameter
     * @param function     the function
     * @param t            the t
     * @param handlingFunc the handling func
     * @return the r
     * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests#testComputeAndHandleException()
     */
    public static <T, R> R computeAndHandleException(ThrowExceptionFunction<T, R> function, T t,
                                                     Function<Exception, R> handlingFunc) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            return handlingFunc.apply(e);
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
     */
    public static <T, U, R> R computeAndHandleException(ThrowExceptionBiFunction<T, U, R> function, T t, U u,
                                                        Function<Exception, R> handlingFunc) {
        try {
            return function.apply(t, u);
        } catch (Exception e) {
            return handlingFunc.apply(e);
        }
    }

    /**
     * Compute and handle exception r.
     *
     * @param <R>          the type parameter
     * @param supplier     the supplier
     * @param handlingFunc the handling func
     * @return the r
     */
    public static <R> R computeAndHandleException(ThrowExceptionSupplier<R> supplier,
                                                  Function<Exception, R> handlingFunc) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return handlingFunc.apply(e);
        }
    }
}
