package com.jmsoftware.maf.common.bean;

/**
 * <h1>EnumerationBase</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 6/6/21 5:38 PM
 **/
public interface EnumerationBase<T extends Number> {
    /**
     * Gets value.
     *
     * @return the value
     */
    T getValue();
}
