package com.jmsoftware.demoone.math.service;

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
     * Add double.
     *
     * @param parameterList the parameter list
     * @return the double
     */
    Double add(List<Double> parameterList);

    /**
     * Subtract double.
     *
     * @param parameter1 the parameter 1
     * @param parameter2 the parameter 2
     * @return the double
     */
    Double subtract(Double parameter1, Double parameter2);
}
