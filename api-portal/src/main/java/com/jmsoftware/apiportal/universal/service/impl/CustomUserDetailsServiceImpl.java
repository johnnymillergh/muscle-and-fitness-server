package com.jmsoftware.apiportal.universal.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.apiportal.remoteapi.authcenter.user.UserRemoteApi;
import com.jmsoftware.apiportal.universal.domain.PermissionPO;
import com.jmsoftware.apiportal.universal.domain.RolePO;
import com.jmsoftware.apiportal.universal.domain.UserPO;
import com.jmsoftware.apiportal.universal.domain.UserPrincipal;
import com.jmsoftware.apiportal.universal.mapper.PermissionMapper;
import com.jmsoftware.apiportal.universal.service.RoleService;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenPayload;
import com.jmsoftware.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
    private final RoleService roleService;
    private final PermissionMapper permissionMapper;
    private final UserRemoteApi userRemoteApi;

    @Override
    public UserDetails loadUserByUsername(String credentials) throws UsernameNotFoundException {
        val payload = new GetUserByLoginTokenPayload();
        payload.setLoginToken(credentials);
        var response = userRemoteApi.getUserByLoginToken(payload);
        val data = response.getData();
        if (ObjectUtil.isNull(data)) {
            String errorMessage = "User's account not found: " + credentials;
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }
        List<RolePO> rolesByUserId = roleService.getRolesByUserId(data.getId());
        if (CollUtil.isEmpty(rolesByUserId)) {
            throw new SecurityException(HttpStatus.ROLE_NOT_FOUND);
        }
        List<Long> roleIds = rolesByUserId.stream()
                .map(RolePO::getId)
                .collect(Collectors.toList());
        List<PermissionPO> permissionList = permissionMapper.selectByRoleIdList(roleIds);
        var user = new UserPO();
        BeanUtil.copyProperties(data, user);
        return UserPrincipal.create(user, rolesByUserId, permissionList);
    }
}
