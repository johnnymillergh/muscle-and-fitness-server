package com.jmsoftware.maf.authcenter.permission.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.jmsoftware.maf.authcenter.permission.mapper.PermissionMapper
import com.jmsoftware.maf.authcenter.permission.persistence.Permission
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType
import org.springframework.stereotype.Service

/**
 * # PermissionDomainServiceImpl
 *
 * Domain Service implementation of Permission. (Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/10/22 11:53 AM
 */
@Service
class PermissionDomainServiceImpl
    : ServiceImpl<PermissionMapper, Permission>(), PermissionDomainService {
    override fun getPermissionListByRoleIdList(
        roleIdList: List<Long>,
        permissionTypeList: List<PermissionType>
    ): List<Permission> {
        return getBaseMapper().selectPermissionListByRoleIdList(roleIdList, permissionTypeList)
    }
}
