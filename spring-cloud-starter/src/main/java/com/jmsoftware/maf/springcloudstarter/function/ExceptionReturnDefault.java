package com.jmsoftware.maf.springcloudstarter.function;

import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionFunction;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.ThrowExceptionSupplier;

/**
 * <h1>ExceptionReturnDefault</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:52 AM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-3'>Java 函数式编程最佳实践 - 赋予函数报错返回默认值能力</a>
 **/
public class ExceptionReturnDefault {
    private ExceptionReturnDefault() {
    }

    /**
     * Compute or get default r.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param function the function
     * @param t        the t
     * @param r        the r
     * @return the r
     * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests#testComputeOrGetDefault()
     */
    public static <T, R> R computeOrGetDefault(ThrowExceptionFunction<T, R> function, T t, R r) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            return r;
        }
    }

    /**
     * Compute or get default r.
     *
     * @param <R>      the type parameter
     * @param supplier the supplier
     * @param r        the r
     * @return the r
     */
    public static <R> R computeOrGetDefault(ThrowExceptionSupplier<R> supplier, R r) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return r;
        }
    }
}