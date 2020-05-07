package com.jmsoftware.apiportal.universal.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * <h1>JwtService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/7/20 5:15 PM
 */
public interface JwtService {
    /**
     * Create JWT string.
     *
     * @param authentication the authentication
     * @param rememberMe     the remember me
     * @return the string
     */
    String createJwt(Authentication authentication, Boolean rememberMe);

    /**
     * Create JWT string.
     *
     * @param rememberMe  the remember me
     * @param id          the user ID
     * @param subject     the username
     * @param roles       the roles
     * @param authorities the authorities
     * @return the JWT string
     */
    String createJwt(Boolean rememberMe, Long id, String subject, List<String> roles, Collection<?
            extends GrantedAuthority> authorities);

    /**
     * Parse JWT.
     *
     * @param jwt the jwt
     * @return the claims
     */
    Claims parseJwt(String jwt);

    /**
     * Invalidate jwt.
     *
     * @param request the request
     */
    void invalidateJwt(HttpServletRequest request);

    /**
     * Gets username from jwt.
     *
     * @param jwt the jwt
     * @return the username from jwt
     */
    String getUsernameFromJwt(String jwt);

    /**
     * Gets username from request.
     *
     * @param request the request
     * @return the username from request
     */
    String getUsernameFromRequest(HttpServletRequest request);

    /**
     * Gets jwt from request.
     *
     * @param request the request
     * @return the jwt from request
     */
    String getJwtFromRequest(HttpServletRequest request);
}
