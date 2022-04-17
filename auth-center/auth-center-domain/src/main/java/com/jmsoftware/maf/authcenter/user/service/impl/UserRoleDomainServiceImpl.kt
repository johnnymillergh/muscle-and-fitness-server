package com.jmsoftware.maf.authcenter.user.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.jmsoftware.maf.authcenter.role.persistence.Role
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.authcenter.user.mapper.UserRoleMapper
import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.authcenter.user.persistence.UserRole
import com.jmsoftware.maf.authcenter.user.service.UserRoleDomainService
import com.jmsoftware.maf.common.exception.InternalServerException
import com.jmsoftware.maf.springcloudstarter.function.requireTrue
import org.springframework.stereotype.Service
import java.util.*

/**
 * # UserRoleDomainServiceImplKt
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 4/12/22 10:28 AM
 **/
@Service
class UserRoleDomainServiceImpl(
    private val roleDomainService: RoleDomainService
) : ServiceImpl<UserRoleMapper, UserRole>(), UserRoleDomainService {
    override fun assignRoleByRoleName(user: User, roleName: String) {
        val role = Optional.ofNullable(
            roleDomainService.ktQuery()
                .select(Role::id)
                .eq(Role::name, roleName)
                .one()
        ).orElseThrow { InternalServerException("Cannot find the role: $roleName") }
        val userRole = UserRole()
        userRole.userId = user.id
        userRole.roleId = role.id
        requireTrue(save(userRole), null).orElseThrow {
            InternalServerException("Cannot assign role ($roleName) to user (${user.username})")
        }
    }
}
