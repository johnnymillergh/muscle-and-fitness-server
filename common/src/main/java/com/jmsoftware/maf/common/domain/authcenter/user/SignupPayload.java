package com.jmsoftware.maf.common.domain.authcenter.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * Description: RegisterPayload, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/22/2020 6:27 PM
 **/
@Data
public class SignupPayload {
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
    @Length(max = 100)
    private String email;
    /**
     * Password
     */
    @NotEmpty
    @Length(min = 8, max = 30)
    private String password;
}
