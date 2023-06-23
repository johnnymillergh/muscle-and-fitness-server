package com.jmsoftware.maf.authcenter.user.service

import com.baomidou.mybatisplus.extension.service.IService
import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.authcenter.user.persistence.UserRole
import org.springframework.validation.annotation.Validated
import jakarta.validation.constraints.NotBlank

/**
 * # UserRoleDomainService
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 10:09 AM
 */
@Validated
interface UserRoleDomainService : IService<UserRole> {
    /**
     * Assign role by role name.
     *
     * @param user     the user
     * @param roleName the role name
     */
    fun assignRoleByRoleName(user: User, roleName: @NotBlank String)
}
