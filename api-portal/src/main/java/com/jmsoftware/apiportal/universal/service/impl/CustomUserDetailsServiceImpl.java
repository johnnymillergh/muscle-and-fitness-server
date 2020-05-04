package com.jmsoftware.apiportal.universal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.jmsoftware.apiportal.universal.domain.RolePO;
import com.jmsoftware.apiportal.universal.domain.UserPO;
import com.jmsoftware.apiportal.universal.domain.UserPrincipal;
import com.jmsoftware.apiportal.universal.mapper.PermissionMapper;
import com.jmsoftware.apiportal.universal.mapper.PermissionPO;
import com.jmsoftware.apiportal.universal.mapper.UserMapper;
import com.jmsoftware.apiportal.universal.service.RoleService;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>CustomUserDetailsServiceImpl</h1>
 * <p>Custom user detail service.</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-03 13:40
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String credentials) throws UsernameNotFoundException {
        UserPO user = userMapper.selectByUsernameOrEmailOrCellphone(credentials, credentials, credentials)
                                .orElseThrow(() -> {
                                    String errorMessage = "User's account not found: " + credentials;
                                    log.error(errorMessage);
                                    return new UsernameNotFoundException(errorMessage);
                                });
        List<RolePO> rolesByUserId = roleService.getRolesByUserId(user.getId());
        if (CollUtil.isEmpty(rolesByUserId)) {
            throw new SecurityException(HttpStatus.ROLE_NOT_FOUND);
        }
        List<Long> roleIds = rolesByUserId.stream()
                                          .map(RolePO::getId)
                                          .collect(Collectors.toList());
        List<PermissionPO> permissionList = permissionMapper.selectByRoleIdList(roleIds);
        return UserPrincipal.create(user, rolesByUserId, permissionList);
    }
}
