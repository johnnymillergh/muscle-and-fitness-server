package com.jmsoftware.maf.springcloudstarter.function;

import cn.hutool.core.util.BooleanUtil;
import com.jmsoftware.maf.springcloudstarter.function.functionalinterface.OrElseThrowExceptionFunction;

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
     */
    public static OrElseThrowExceptionFunction requireTrue(Boolean aBoolean, Consumer<Boolean> after) {
        if (nonNull(after)) {
            after.accept(aBoolean);
        }
        return exceptionSupplier -> {
            if (BooleanUtil.isFalse(aBoolean)) {
                throw exceptionSupplier.get();
            }
        };
    }
}
