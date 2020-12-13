package com.jmsoftware.maf.common.domain.authcenter.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * <h1>SaveUserForRegisteringPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/11/20 5:34 AM
 **/
@Data
public class SaveUserForRegisteringPayload {
    /**
     * The Username.
     */
    @NotEmpty
    @Length(min = 4, max = 50)
    private String username;
    /**
     * The Email.
     */
    @NotEmpty
    @Length(max = 100)
    private String email;
    /**
     * The Encoded password.
     */
    @NotEmpty
    @Length(min = 8, max = 60)
    private String encodedPassword;
}
