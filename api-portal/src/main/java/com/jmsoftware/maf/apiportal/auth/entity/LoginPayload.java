package com.jmsoftware.maf.apiportal.auth.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <h1>LoginPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/8/20 2:08 PM
 **/
@Data
public class LoginPayload {
    /**
     * The Login token: username / email
     */
    @NotEmpty
    @Length(max = 100)
    private String loginToken;
    /**
     * The Password.
     */
    @NotEmpty
    @Length(max = 60)
    private String password;
    /**
     * Remember me
     */
    @NotNull
    private Boolean rememberMe;
}
