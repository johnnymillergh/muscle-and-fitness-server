package com.jmsoftware.maf.authcenter.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmsoftware.maf.authcenter.user.persistence.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <h1>UserRoleMapper</h1>
 * <p>
 * Mapper of user_role.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/4/2021 1:24 PM
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
