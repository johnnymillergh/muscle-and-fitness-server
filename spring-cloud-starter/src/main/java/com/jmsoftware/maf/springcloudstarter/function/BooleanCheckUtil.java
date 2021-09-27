package com.jmsoftware.maf.springcloudstarter.function;

import cn.hutool.core.util.BooleanUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

/**
 * Description: BooleanCheckUtil, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/27/2021 12:03 PM
 **/
public class BooleanCheckUtil {
    /**
     * Require true or else throw exception.
     *
     * @param aBoolean the aBoolean
     * @param after    the after
     * @return the throw exception function
     */
    public static ThrowExceptionFunction requireTrue(Boolean aBoolean, Consumer<Boolean> after) {
        if (nonNull(after)) {
            after.accept(aBoolean);
        }
        return exceptionSupplier -> {
            if (BooleanUtil.isFalse(aBoolean)) {
                throw exceptionSupplier.get();
            }
        };
    }

    @FunctionalInterface
    public interface ThrowExceptionFunction {
        /**
         * Or else throw.
         *
         * @param exceptionSupplier the exception supplier
         * @throws Throwable the throwable
         */
        void orElseThrow(Supplier<? extends Throwable> exceptionSupplier) throws Throwable;
    }
}
