package com.jmsoftware.maf.authcenter.user.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.jmsoftware.maf.authcenter.user.persistence.UserRole
import org.apache.ibatis.annotations.Mapper

/**
 * # UserRoleMapper
 *
 * Mapper of user_role.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:45 PM
 */
@Mapper
interface UserRoleMapper : BaseMapper<UserRole>
