package com.jmsoftware.maf.springcloudstarter.function.functionalinterface;

/**
 * <h1>ThrowExceptionFunction</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:47 AM
 **/
@FunctionalInterface
public interface ThrowExceptionFunction<T, R> {
    /**
     * Apply r.
     *
     * @param t the t
     * @return the r
     * @throws Exception the exception
     */
    R apply(T t) throws Exception;
}
