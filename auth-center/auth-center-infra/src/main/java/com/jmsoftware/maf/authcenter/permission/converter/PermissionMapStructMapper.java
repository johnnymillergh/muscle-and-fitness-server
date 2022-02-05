/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
package com.jmsoftware.maf.authcenter.permission.converter;

import com.jmsoftware.maf.authcenter.permission.persistence.Permission;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.factory.Mappers.getMapper;

/**
 * Description: PermissionMapStructMapper, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/5/2022 5:33 PM
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
