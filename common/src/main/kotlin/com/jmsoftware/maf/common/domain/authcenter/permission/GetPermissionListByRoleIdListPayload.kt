package com.jmsoftware.maf.common.domain.authcenter.permission

import javax.validation.constraints.NotEmpty

/**
 * # GetPermissionListByRoleIdListPayload
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 6:41 PM
 */
class GetPermissionListByRoleIdListPayload {
    @NotEmpty
    lateinit var roleIdList: List<Long>

    @NotEmpty
    lateinit var permissionTypeList: List<PermissionType>
}
