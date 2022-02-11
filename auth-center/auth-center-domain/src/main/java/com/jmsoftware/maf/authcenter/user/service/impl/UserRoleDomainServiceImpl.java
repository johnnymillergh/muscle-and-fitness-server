package com.jmsoftware.maf.authcenter.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.role.persistence.Role;
import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.authcenter.user.mapper.UserRoleMapper;
import com.jmsoftware.maf.authcenter.user.persistence.User;
import com.jmsoftware.maf.authcenter.user.persistence.UserRole;
import com.jmsoftware.maf.authcenter.user.service.UserRoleDomainService;
import com.jmsoftware.maf.common.exception.BizException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

/**
 * UserRoleDomainServiceImpl.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/29/2021 12:09 PM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleDomainServiceImpl
        extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleDomainService {
    private final RoleDomainService roleDomainService;

    @Override
    @SneakyThrows({BizException.class})
    public void assignRoleByRoleName(@NonNull User user, @NotBlank String roleName) {
        val queryWrapper = Wrappers.lambdaQuery(Role.class);
        queryWrapper.select(Role::getId)
                .eq(Role::getName, roleName);
        val role = Optional.ofNullable(this.roleDomainService.getOne(queryWrapper))
                .orElseThrow(() -> new BizException("Cannot find the role: " + roleName));
        val userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        val saved = this.save(userRole);
        if (!saved) {
            throw new BizException(String.format("Cannot assign role (%s) to user (%s)", roleName, user.getUsername()));
        }
    }
}
