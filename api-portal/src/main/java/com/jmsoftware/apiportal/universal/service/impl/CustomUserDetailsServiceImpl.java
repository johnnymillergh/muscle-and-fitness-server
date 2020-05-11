package com.jmsoftware.apiportal.universal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.apiportal.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.apiportal.universal.domain.UserPrincipal;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenPayload;
import com.jmsoftware.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    private final AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String credentials) throws UsernameNotFoundException {
        val payload = new GetUserByLoginTokenPayload();
        payload.setLoginToken(credentials);
        val response = authCenterRemoteApi.getUserByLoginToken(payload);
        val data = response.getData();
        if (ObjectUtil.isNull(data)) {
            val errorMessage = String.format("User's account not found, credentials: %s", credentials);
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }
        val payload1 = new GetRoleListByUserIdPayload();
        payload1.setUserId(data.getId());
        val roleListByUserIdResponse = authCenterRemoteApi.getRoleListByUserId(payload1);
        val roleList = roleListByUserIdResponse.getData().getRoleList();
        if (CollUtil.isEmpty(roleList)) {
            throw new SecurityException(HttpStatus.ROLE_NOT_FOUND);
        }
        val payload2 = new GetPermissionListByRoleIdListPayload();
        roleList.forEach(role -> {
            payload2.getRoleIdList().add(role.getId());
        });
        val permissionListByRoleIdListResponse = authCenterRemoteApi.getPermissionListByRoleIdList(payload2);
        val roleNameList =
                roleList.stream().map(GetRoleListByUserIdResponse.Role::getName).collect(Collectors.toList());
        return UserPrincipal.create(data, roleNameList,
                                    permissionListByRoleIdListResponse.getData().getPermissionList());
    }
}
