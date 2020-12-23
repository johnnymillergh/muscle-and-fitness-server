package com.jmsoftware.maf.apigateway.universal.service;

import com.jmsoftware.maf.apigateway.universal.domain.ValidationTestPayload;

import java.util.Map;

/**
 * <h1>CommonService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/4/20 11:15 AM
 */
public interface CommonService {
    /**
     * Gets application info.
     *
     * @return the application info.
     */
    Map<String, Object> getApplicationInfo();

    /**
     * Validate object.
     *
     * @param payload the payload
     */
    void validateObject(ValidationTestPayload payload);
}