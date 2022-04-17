package com.jmsoftware.maf.common.domain.authcenter.user

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

/**
 * # SignupPayload
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:11 PM
 */
class SignupPayload {
    /**
     * The Username.
     */
    @NotBlank
    @Length(min = 4, max = 50)
    lateinit var username: String

    /**
     * The Email.
     */
    @Email
    @NotBlank
    @Length(max = 100)
    lateinit var email: String

    /**
     * The Encoded password.
     */
    @NotBlank
    @Length(min = 8, max = 60)
    lateinit var password: String
}
