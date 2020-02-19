package com.jmsoftware.demotwo.math.service;

import java.util.List;

/**
 * <h1>MathService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/17/20 8:23 AM
 */
public interface MathService {
    /**
     * Multiply double.
     *
     * @param parameterList the parameter list
     * @return the double
     */
    Double multiply(List<Double> parameterList);

    /**
     * Divide double.
     *
     * @param parameter1 the parameter 1
     * @param parameter2 the parameter 2
     * @return the double
     */
    Double divide(Double parameter1, Double parameter2);
}
