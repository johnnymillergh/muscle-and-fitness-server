package com.jmsoftware.apiportal.auth.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * <h1>RegisterPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/9/20 3:53 PM
 **/
@Data
public class RegisterPayload {
    /**
     * Username (Unique)
     */
    @NotEmpty
    @Length(min = 4, max = 50)
    private String username;
    /**
     * Email (Unique)
     */
    @NotEmpty
    @Size(max = 30)
    private String email;
    /**
     * Password
     */
    @NotEmpty
    @Length(min = 8, max = 30)
    private String password;
}
