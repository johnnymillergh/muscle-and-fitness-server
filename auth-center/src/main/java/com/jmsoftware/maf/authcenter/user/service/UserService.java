package com.jmsoftware.maf.authcenter.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.authcenter.user.entity.GetUserStatusPayload;
import com.jmsoftware.maf.authcenter.user.entity.UserPersistence;
import com.jmsoftware.maf.common.domain.authcenter.user.*;
import com.jmsoftware.maf.common.exception.SecurityException;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>UserService</h1>
 * <p>
 * Service of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5 /10/20 12:31 PM
 */
@Validated
public interface UserService extends IService<UserPersistence> {
    /**
     * Gets user by login token.
     *
     * @param loginToken the login token
     * @return the user by login token
     */
    GetUserByLoginTokenResponse getUserByLoginToken(@NotBlank String loginToken);

    /**
     * Save user for registering save user for registering response.
     *
     * @param payload the payload
     * @return the save user for registering response
     */
    SignupResponse saveUserForSignup(@Valid SignupPayload payload);

    /**
     * Login login response.
     *
     * @param payload the payload
     * @return the login response
     * @throws SecurityException the security exception
     */
    LoginResponse login(@Valid LoginPayload payload) throws SecurityException;

    /**
     * Logout boolean.
     *
     * @param request the request
     * @return the boolean
     * @throws SecurityException the security exception
     */
    boolean logout(HttpServletRequest request) throws SecurityException;

    /**
     * Gets user status.
     *
     * @param payload the payload
     * @return the user status
     */
    String getUserStatus(@Valid @NotNull GetUserStatusPayload payload);
}
