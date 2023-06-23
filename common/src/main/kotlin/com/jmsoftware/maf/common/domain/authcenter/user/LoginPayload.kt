package com.jmsoftware.maf.common.domain.authcenter.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

/**
 * Description: LoginPayload, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:08 PM
 */
class LoginPayload {
    /**
     * The Login token: username / email
     */
    @NotBlank
    @Length(max = 100)
    lateinit var loginToken: String

    /**
     * The Password.
     */
    @NotBlank
    @Length(max = 60)
    lateinit var password: String

    /**
     * Remember me
     */
    @NotNull
    var rememberMe: Boolean = false
}
