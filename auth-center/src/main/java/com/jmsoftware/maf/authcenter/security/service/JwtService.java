package com.jmsoftware.maf.authcenter.security.service;

import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtPayload;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.exception.SecurityException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * <h1>JwtService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 10:44 AM
 */
@Validated
public interface JwtService {
    /**
     * Create JWT string.
     *
     * @param authentication the authentication
     * @param rememberMe     the remember me
     * @return the string
     * @throws SecurityException the security exception
     */
    String createJwt(Authentication authentication, Boolean rememberMe) throws SecurityException;

    /**
     * Create JWT string.
     *
     * @param rememberMe  the remember me
     * @param id          the user ID
     * @param subject     the username
     * @param roles       the roles
     * @param authorities the authorities
     * @return the JWT string
     * @throws SecurityException the security exception
     */
    String createJwt(Boolean rememberMe, Long id, String subject, List<String> roles, Collection<?
            extends GrantedAuthority> authorities) throws SecurityException;

    /**
     * Parse JWT.
     *
     * @param jwt the jwt
     * @return the claims
     * @throws SecurityException the security exception
     */
    Claims parseJwt(String jwt) throws SecurityException;

    /**
     * Invalidate jwt.
     *
     * @param request the request
     * @throws SecurityException the security exception
     */
    void invalidateJwt(HttpServletRequest request) throws SecurityException;

    /**
     * Gets username from jwt.
     *
     * @param jwt the jwt
     * @return the username from jwt
     * @throws SecurityException the security exception
     */
    String getUsernameFromJwt(String jwt) throws SecurityException;

    /**
     * Gets username from request.
     *
     * @param request the request
     * @return the username from request
     * @throws SecurityException the security exception
     */
    String getUsernameFromRequest(HttpServletRequest request) throws SecurityException;

    /**
     * Gets jwt from request.
     *
     * @param request the request
     * @return the jwt from request
     */
    String getJwtFromRequest(HttpServletRequest request);

    /**
     * Parse parse jwt response.
     *
     * @param payload the payload
     * @return the parse jwt response
     * @throws SecurityException the security exception
     */
    ParseJwtResponse parse(@Valid ParseJwtPayload payload) throws SecurityException;
}
