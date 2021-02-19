package com.jmsoftware.maf.common.domain.authcenter.user;

import lombok.Data;

/**
 * Description: LoginResponse, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/22/2020 6:27 PM
 **/
@Data
public class LoginResponse {
    private String greeting;
    private String jwt;
}
