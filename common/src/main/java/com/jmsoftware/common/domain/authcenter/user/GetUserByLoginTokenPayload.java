package com.jmsoftware.common.domain.authcenter.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * <h1>GetUserByLoginTokenPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/10/20 12:41 PM
 **/
@Data
public class GetUserByLoginTokenPayload {
    /**
     * The Login token: username / email
     */
    @NotEmpty
    @Length(max = 100)
    private String loginToken;
}
