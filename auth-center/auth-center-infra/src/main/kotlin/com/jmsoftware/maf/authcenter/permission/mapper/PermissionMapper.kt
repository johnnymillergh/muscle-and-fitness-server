package com.jmsoftware.maf.authcenter.permission.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.jmsoftware.maf.authcenter.permission.persistence.Permission
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * # PermissionMapper
 *
 * Mapper of Permission. (Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 6:06 PM
 */
@Mapper
interface PermissionMapper : BaseMapper<Permission> {
    /**
     * Select permission list by role id list.
     *
     * @param roleIdList         the role id list
     * @param permissionTypeList the permission type list
     * @return the list
     */
    fun selectPermissionListByRoleIdList(
        @Param("roleIdList") roleIdList: List<Long>,
        @Param("permissionTypeList") permissionTypeList: List<PermissionType>
    ): List<Permission>
}
