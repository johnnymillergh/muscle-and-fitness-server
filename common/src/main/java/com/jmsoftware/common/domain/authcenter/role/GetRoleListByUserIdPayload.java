package com.jmsoftware.common.domain.authcenter.role;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <h1>GetRoleListByUserIdPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/10/20 10:52 PM
 **/
@Data
public class GetRoleListByUserIdPayload {
    @NotNull
    private Long userId;
}
