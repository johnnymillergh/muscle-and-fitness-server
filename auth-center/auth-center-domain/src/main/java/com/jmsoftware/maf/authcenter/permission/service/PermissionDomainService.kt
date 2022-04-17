package com.jmsoftware.maf.authcenter.permission.service

import com.baomidou.mybatisplus.extension.service.IService
import com.jmsoftware.maf.authcenter.permission.persistence.Permission
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

/**
 * # PermissionDomainService
 *
 * Domain Service of Permission. (Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/10/22 11:50 AM
 */
@Validated
interface PermissionDomainService : IService<Permission> {
    /**
     * Gets permission list by role id list.
     *
     * @param roleIdList         the role id list
     * @param permissionTypeList the permission type list
     * @return the permission list by role id list
     */
    fun getPermissionListByRoleIdList(
        roleIdList: @NotEmpty List<Long>,
        permissionTypeList: @NotEmpty List<PermissionType>
    ): List<Permission>
}
