package com.jmsoftware.maf.authcenter.user.service

import com.baomidou.mybatisplus.extension.service.IService
import com.jmsoftware.maf.authcenter.user.payload.GetUserPageListPayload
import com.jmsoftware.maf.authcenter.user.payload.GetUserStatusPayload
import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.user.*
import com.jmsoftware.maf.common.exception.SecurityException
import org.springframework.validation.annotation.Validated
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * # UserDomainService
 *
 * Service of UserPersistence. (UserPersistence)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 7:41 AM
 */
@Validated
interface UserDomainService : IService<User> {
    /**
     * Gets user by login token.
     *
     * @param loginToken the login token
     * @return the user by login token
     */
    fun getUserByLoginToken(loginToken: @NotBlank String): GetUserByLoginTokenResponse?

    /**
     * Save user for registering save user for registering response.
     *
     * @param payload the payload
     * @return the save user for registering response
     */
    fun saveUserForSignup(payload: @Valid SignupPayload): SignupResponse

    /**
     * Login login response.
     *
     * @param payload the payload
     * @return the login response
     * @throws SecurityException the security exception
     */
    fun login(payload: @Valid LoginPayload): LoginResponse

    /**
     * Logout boolean.
     *
     * @param request the request
     * @return the boolean
     * @throws SecurityException the security exception
     */
    fun logout(request: HttpServletRequest): Boolean

    /**
     * Gets user status.
     *
     * @param payload the payload
     * @return the user status
     */
    fun getUserStatus(payload: @Valid @NotNull GetUserStatusPayload): String

    /**
     * Gets user page list.
     *
     * @param payload the payload
     * @return the user page list
     */
    fun getUserPageList(payload: @Valid @NotNull GetUserPageListPayload): PageResponseBodyBean<User>
}
