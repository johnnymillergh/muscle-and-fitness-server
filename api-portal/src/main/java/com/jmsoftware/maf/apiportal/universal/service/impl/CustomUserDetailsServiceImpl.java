package com.jmsoftware.maf.apiportal.universal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.maf.apiportal.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.apiportal.universal.domain.UserPrincipal;
import com.jmsoftware.maf.common.constant.HttpStatus;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdPayload;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.exception.SecurityException;
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
        val getUserByLoginTokenResponseResponseBody = authCenterRemoteApi.getUserByLoginToken(credentials);
        val getUserByLoginTokenResponse = getUserByLoginTokenResponseResponseBody.getData();
        if (ObjectUtil.isNull(getUserByLoginTokenResponse)) {
            val errorMessage = String.format("User's account not found, credentials: %s", credentials);
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }
        val getRoleListByUserIdPayload = new GetRoleListByUserIdPayload();
        getRoleListByUserIdPayload.setUserId(getUserByLoginTokenResponse.getId());
        val getRoleListByUserIdResponseResponseBody =
                authCenterRemoteApi.getRoleListByUserId(getRoleListByUserIdPayload);
        val roleList = getRoleListByUserIdResponseResponseBody.getData().getRoleList();
        if (CollUtil.isEmpty(roleList)) {
            throw new SecurityException(HttpStatus.ROLE_NOT_FOUND);
        }
        val getPermissionListByRoleIdListPayload = new GetPermissionListByRoleIdListPayload();
        roleList.forEach(role -> getPermissionListByRoleIdListPayload.getRoleIdList().add(role.getId()));
        val permissionListByRoleIdListResponseBody =
                authCenterRemoteApi.getPermissionListByRoleIdList(getPermissionListByRoleIdListPayload);
        val roleNameList =
                roleList.stream().map(GetRoleListByUserIdResponse.Role::getName).collect(Collectors.toList());
        return UserPrincipal.create(getUserByLoginTokenResponse, roleNameList,
                                    permissionListByRoleIdListResponseBody.getData().getPermissionList());
    }
}
