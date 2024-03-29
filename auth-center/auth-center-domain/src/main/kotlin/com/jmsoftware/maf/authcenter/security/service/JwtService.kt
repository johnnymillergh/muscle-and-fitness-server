package com.jmsoftware.maf.authcenter.security.service

import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.exception.SecurityException
import io.jsonwebtoken.Claims
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.validation.annotation.Validated
import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * # JwtService
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 8:47 PM
 */
@Validated
interface JwtService {
    /**
     * Create JWT string.
     *
     * @param authentication the authentication
     * @param rememberMe     the remember me
     * @return the string
     */
    fun createJwt(@NotNull authentication: Authentication, @NotNull rememberMe: Boolean): String

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
    fun createJwt(
        @NotNull rememberMe: Boolean,
        @NotNull id: Long,
        @NotBlank subject: String,
        roles: List<String>,
        authorities: Collection<GrantedAuthority>
    ): String

    /**
     * Parse JWT.
     *
     * @param jwt the jwt
     * @return the claims
     * @throws SecurityException the security exception
     */
    fun parseJwt(@NotBlank jwt: String): Claims

    /**
     * Invalidate jwt.
     *
     * @param request the request
     * @throws SecurityException the security exception
     */
    fun invalidateJwt(@NotNull request: HttpServletRequest)

    /**
     * Gets username from jwt.
     *
     * @param jwt the jwt
     * @return the username from jwt
     * @throws SecurityException the security exception
     */
    fun getUsernameFromJwt(@NotBlank jwt: String): String

    /**
     * Gets username from request.
     *
     * @param request the request
     * @return the username from request
     * @throws SecurityException the security exception
     */
    fun getUsernameFromRequest(@NotNull request: HttpServletRequest): String

    /**
     * Gets jwt from request.
     *
     * @param request the request
     * @return the jwt from request
     */
    fun getJwtFromRequest(@NotNull request: HttpServletRequest): String

    /**
     * Parse parse jwt response.
     *
     * @param request the request
     * @return the parse jwt response
     */
    fun parse(@NotNull request: HttpServletRequest): ParseJwtResponse
}
