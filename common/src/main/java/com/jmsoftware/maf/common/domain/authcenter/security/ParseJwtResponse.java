package com.jmsoftware.maf.common.domain.authcenter.security;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description: ParseJwtResponse, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/29/2020 11:09 AM
 **/
@Data
@Accessors(chain = true)
public class ParseJwtResponse {
    private String username;
}
