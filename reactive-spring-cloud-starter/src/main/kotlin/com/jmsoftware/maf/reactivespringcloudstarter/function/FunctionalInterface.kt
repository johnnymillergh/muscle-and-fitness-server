package com.jmsoftware.maf.reactivespringcloudstarter.function

import java.util.function.Supplier

/**
 * # OrElseThrowExceptionFunction
 *
 * Description: OrElseThrowExceptionFunction, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:32 PM
 */
fun interface OrElseThrowExceptionFunction {
    /**
     * Or else throw.
     *
     * @param exceptionSupplier the exception supplier
     * @throws Throwable the throwable
     */
    fun orElseThrow(exceptionSupplier: Supplier<Throwable>)
}

/**
 * # ThrowExceptionBiFunction
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:35 PM
 */
fun interface ThrowExceptionBiFunction<T, U, R> {
    /**
     * Apply r.
     *
     * @param t the t
     * @param u the u
     * @return the r
     * @throws Exception the exception
     */
    fun apply(t: T, u: U): R
}

/**
 * # ThrowExceptionFunction
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:35 PM
 */
fun interface ThrowExceptionFunction<T, R> {
    /**
     * Apply r.
     *
     * @param t the t
     * @return the r
     * @throws Exception the exception
     */
    fun apply(t: T): R
}

/**
 * # ThrowExceptionRunnable
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:36 PM
 */
fun interface ThrowExceptionRunnable {
    /**
     * Run.
     *
     * @throws Exception the exception
     */
    fun run()
}

/**
 * # ThrowExceptionSupplier
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:36 PM
 */
fun interface ThrowExceptionSupplier<T> {
    /**
     * Get t.
     *
     * @return the t
     * @throws Exception the exception
     */
    fun get(): T
}
