package com.jmsoftware.maf.springcloudstarter.function.functionalinterface;

import java.util.function.Supplier;

/**
 * Description: OrElseThrowExceptionFunction, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/28/2021 10:01 AM
 **/
@FunctionalInterface
public interface OrElseThrowExceptionFunction {
    /**
     * Or else throw.
     *
     * @param exceptionSupplier the exception supplier
     * @throws Throwable the throwable
     */
    void orElseThrow(Supplier<? extends Throwable> exceptionSupplier) throws Throwable;
}
