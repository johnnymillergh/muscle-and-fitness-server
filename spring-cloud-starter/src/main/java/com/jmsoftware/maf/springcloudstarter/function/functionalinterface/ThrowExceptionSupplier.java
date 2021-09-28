package com.jmsoftware.maf.springcloudstarter.function.functionalinterface;

/**
 * <h1>ThrowExceptionSupplier</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:49 AM
 **/
@FunctionalInterface
public interface ThrowExceptionSupplier<T> {
    /**
     * Get t.
     *
     * @return the t
     * @throws Exception the exception
     */
    T get() throws Exception;
}
