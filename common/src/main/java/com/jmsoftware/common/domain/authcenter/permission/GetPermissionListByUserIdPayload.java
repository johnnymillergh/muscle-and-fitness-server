package com.jmsoftware.common.domain.authcenter.permission;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <h1>GetPermissionListByUserIdPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/12/20 9:03 AM
 **/
@Data
public class GetPermissionListByUserIdPayload {
    /**
     * The User id.
     */
    @NotNull
    private Long userId;
}
