package com.jmsoftware.maf.authcenter.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.authcenter.user.persistence.User;
import com.jmsoftware.maf.authcenter.user.persistence.UserRole;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * UserRoleDomainService.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/29/2021 12:09 PM
 */
@Validated
public interface UserRoleDomainService extends IService<UserRole> {
    /**
     * Assign role by role name.
     *
     * @param user     the user
     * @param roleName the role name
     */
    void assignRoleByRoleName(@NonNull User user, @NotBlank String roleName);
}
