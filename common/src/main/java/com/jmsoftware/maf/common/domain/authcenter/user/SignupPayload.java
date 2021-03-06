package com.jmsoftware.maf.common.domain.authcenter.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <h1>SignupPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/11/20 5:34 AM
 **/
@Data
public class SignupPayload {
    /**
     * The Username.
     */
    @NotBlank
    @Length(min = 4, max = 50)
    private String username;
    /**
     * The Email.
     */
    @NotBlank
    @Length(max = 100)
    private String email;
    /**
     * The Encoded password.
     */
    @NotBlank
    @Length(min = 8, max = 60)
    private String password;
}
