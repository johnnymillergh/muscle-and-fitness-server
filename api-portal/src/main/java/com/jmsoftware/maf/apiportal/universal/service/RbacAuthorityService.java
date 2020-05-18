package com.jmsoftware.maf.apiportal.universal.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>RbacAuthorityService</h1>
 * <p>Route Authority service interface</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-04-07 19:52
 **/
public interface RbacAuthorityService {
    /**
     * Check whether the user from request has permission
     *
     * @param request        HTTP request
     * @param authentication authentication
     * @return true - the user has permission; false - the user do not have permission
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
