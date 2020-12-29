package com.jmsoftware.maf.common.domain.authcenter.security;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description: ParseJwtPayload, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/29/2020 11:09 AM
 **/
@Data
public class ParseJwtPayload {
    @NotBlank
    private String jwt;
}
