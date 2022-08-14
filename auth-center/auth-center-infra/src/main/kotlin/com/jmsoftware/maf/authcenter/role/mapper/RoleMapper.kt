package com.jmsoftware.maf.authcenter.role.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.jmsoftware.maf.authcenter.role.persistence.Role
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse
import org.apache.ibatis.annotations.Mapper

/**
 * # RoleMapper
 *
 * MyBatis Mapper of Role. (Role)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:39 PM
 */
@Mapper
interface RoleMapper : BaseMapper<Role> {
    /**
     * Select role list by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    fun selectRoleListByUserId(userId: Long): List<GetRoleListByUserIdSingleResponse>

    /**
     * Select by id role persistence.
     *
     * @param roleName the role name
     * @return the role persistence
     */
    fun selectByName(roleName: String): Role
}
