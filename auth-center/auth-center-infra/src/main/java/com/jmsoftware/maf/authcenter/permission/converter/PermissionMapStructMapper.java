package com.jmsoftware.maf.authcenter.permission.converter;

import com.jmsoftware.maf.authcenter.permission.persistence.Permission;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 * Description: PermissionMapStructMapper, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/5/2022 7:16 PM
 **/
@Mapper
public interface PermissionMapStructMapper {
    PermissionMapStructMapper INSTANCE = getMapper(PermissionMapStructMapper.class);

    /**
     * Permission -> GetPermissionListByRoleIdListResponse.Permission
     *
     * @param permission the permission
     * @return the get permission list by role id list response . permission
     */
    GetPermissionListByRoleIdListResponse.Permission of(Permission permission);
}
