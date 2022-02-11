package com.jmsoftware.maf.springcloudstarter.function;

import cn.hutool.core.util.BooleanUtil;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.OrElseThrowExceptionFunction;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

import static java.util.Objects.nonNull;

/**
 * Description: BooleanCheck, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/27/2021 12:03 PM
 **/
public class BooleanCheck {
    private BooleanCheck() {
    }

    /**
     * Require true or else throw exception.
     *
     * @param aBoolean the aBoolean
     * @param after    the after
     * @return the throw exception function
     * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests#testRequireTrue()
     */
    public static OrElseThrowExceptionFunction requireTrue(boolean aBoolean, @Nullable Consumer<Boolean> after) {
        if (nonNull(after)) {
            after.accept(aBoolean);
        }
        return exceptionSupplier -> {
            if (BooleanUtil.isFalse(aBoolean)) {
                throw exceptionSupplier.get();
            }
        };
    }

    /**
     * Require all true or else throw exception function.
     *
     * @param booleans the booleans
     * @return the or else throw exception function
     * @see com.jmsoftware.maf.springcloudstarter.FunctionalInterfaceTests#testRequireTrue()
     */
    public static OrElseThrowExceptionFunction requireAllTrue(boolean... booleans) {
        return exceptionSupplier -> {
            for (boolean aBoolean : booleans) {
                if (BooleanUtil.isFalse(aBoolean)) {
                    throw exceptionSupplier.get();
                }
            }
        };
    }
}
