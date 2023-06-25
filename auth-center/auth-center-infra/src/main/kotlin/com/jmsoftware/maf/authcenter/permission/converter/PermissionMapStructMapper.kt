package com.jmsoftware.maf.authcenter.permission.converter

import com.jmsoftware.maf.authcenter.permission.persistence.Permission
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers.getMapper

/**
 * Description: PermissionMapStructMapper, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 6:04 PM
 */
@Mapper
interface PermissionMapStructMapper {
    companion object {
        val INSTANCE: PermissionMapStructMapper = getMapper(PermissionMapStructMapper::class.java)
    }

    /**
     * Permission -> GetPermissionListByRoleIdListResponse.Permission
     *
     * @param permission the permission
     * @return the get permission list by role id list response . permission
     */
    fun of(permission: Permission): com.jmsoftware.maf.common.domain.authcenter.permission.Permission

    /**
     * List of [Permission] -> list of [com.jmsoftware.maf.common.domain.authcenter.permission.Permission]
     *
     * @param permissionList the permission list
     * @return the list of [com.jmsoftware.maf.common.domain.authcenter.permission.Permission]
     */
    fun ofList(permissionList: List<Permission>): List<com.jmsoftware.maf.common.domain.authcenter.permission.Permission>
}
