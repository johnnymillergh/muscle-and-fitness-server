package com.jmsoftware.maf.common.domain.authcenter.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Description: LoginPayload, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/22/2020 6:26 PM
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
