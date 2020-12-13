package com.jmsoftware.maf.authcenter.universal.domain;

import lombok.Data;

/**
 * <h1>RolePermissionPO</h1>
 * <p>Role-permission relation. Persistence class for table `t_role_permission`</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 19:50
 **/
@Data
public class RolePermissionPO {
    /**
     * Role's ID.
     */
    private Long roleId;
    /**
     * Permission's ID.
     */
    private Long permissionId;
}
