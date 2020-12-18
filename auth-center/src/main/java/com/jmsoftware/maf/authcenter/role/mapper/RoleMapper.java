package com.jmsoftware.maf.authcenter.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmsoftware.maf.authcenter.role.entity.RolePersistence;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <h1>RoleMapper</h1>
 * <p>
 * Mapper of Role.(Role)
 *
 * @author Johnny Miller (锺俊)
 * @date 2020 -05-10 22:39:48
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePersistence> {
    /**
     * Select role list by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<RolePersistence> selectRoleListByUserId(Long userId);
}
