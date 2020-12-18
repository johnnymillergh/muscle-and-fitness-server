package com.jmsoftware.maf.authcenter.universal.service;

import com.jmsoftware.maf.common.domain.ValidationTestPayload;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Map;

/**
 * <h1>CommonService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/4/20 11:15 AM
 */
@Validated
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
    void validateObject(@Valid ValidationTestPayload payload);
}
