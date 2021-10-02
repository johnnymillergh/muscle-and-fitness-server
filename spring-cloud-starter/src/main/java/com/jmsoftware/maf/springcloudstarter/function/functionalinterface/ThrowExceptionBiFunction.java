package com.jmsoftware.maf.springcloudstarter.function.functionalinterface;

/**
 * <h1>ThrowExceptionBiFunction</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:48 AM
 **/
@FunctionalInterface
public interface ThrowExceptionBiFunction<T, U, R> {
    /**
     * Apply r.
     *
     * @param t the t
     * @param u the u
     * @return the r
     * @throws Exception the exception
     */
    R apply(T t, U u) throws Exception;
}
