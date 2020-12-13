package com.jmsoftware.maf.apiportal.universal.service;

import com.jmsoftware.maf.apiportal.universal.domain.ValidationTestPayload;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Map;

/**
 * <h1>CommonService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
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

    /**
     * Generate jwt string.
     *
     * @param username the username
     * @return the string
     */
    String generateJwt(String username);
}
