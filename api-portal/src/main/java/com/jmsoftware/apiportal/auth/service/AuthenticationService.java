package com.jmsoftware.apiportal.auth.service;

import com.jmsoftware.apiportal.auth.entity.LoginPayload;
import com.jmsoftware.apiportal.auth.entity.LoginResponse;
import com.jmsoftware.apiportal.auth.entity.RegisterPayload;

/**
 * <h1>AuthenticationService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/8/20 2:04 PM
 */
public interface AuthenticationService {
    /**
     * Register.
     *
     * @param payload the payload
     */
    void register(RegisterPayload payload);

    /**
     * Login login response.
     *
     * @param payload the payload
     * @return the login response
     */
    LoginResponse login(LoginPayload payload);
}
