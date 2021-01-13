package com.jmsoftware.maf.authcenter.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmsoftware.maf.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <h1>PermissionMapper</h1>
 * <p>
 * Mapper of Permission.(Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5 /11/20 8:34 AM
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionPersistence> {
    /**
     * Select permission list by role id list list.
     *
     * @param roleIdList         the role id list
     * @param permissionTypeList the permission type list
     * @return the list
     */
    List<PermissionPersistence> selectPermissionListByRoleIdList(@Param("roleIdList") List<Long> roleIdList,
                                                                 @Param("permissionTypeList") List<PermissionType> permissionTypeList);
}
