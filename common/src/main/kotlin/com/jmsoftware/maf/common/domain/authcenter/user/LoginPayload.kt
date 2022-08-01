package com.jmsoftware.maf.common.domain.authcenter.user

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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
