package com.jmsoftware.maf.apiportal.remoteapi;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.SaveUserForRegisteringPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.SaveUserForRegisteringResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <h1>AuthCenterRemoteApi</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/10/20 4:50 PM
 */
@Validated
@FeignClient(name = "auth-center")
public interface AuthCenterRemoteApi {
    /**
     * Gets user by login token.
     *
     * @param loginToken the login token
     * @return the user by login token
     */
    @GetMapping("/user-remote-api/users/{loginToken}")
    ResponseBodyBean<GetUserByLoginTokenResponse> getUserByLoginToken(@PathVariable String loginToken);

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    @GetMapping("/role-remote-api/roles/{userId}")
    ResponseBodyBean<GetRoleListByUserIdResponse> getRoleListByUserId(@PathVariable Long userId);

    /**
     * Save user for registering response body bean.
     *
     * @param payload the payload
     * @return the response body bean
     */
    @PostMapping("/user-remote-api/users")
    ResponseBodyBean<SaveUserForRegisteringResponse> saveUserForRegister(@Valid @RequestBody SaveUserForRegisteringPayload payload);

    /**
     * Gets permission list by role id list.
     *
     * @param payload the payload
     * @return the permission list by role id list
     */
    @GetMapping("/permission-remote-api/permissions")
    ResponseBodyBean<GetPermissionListByRoleIdListResponse> getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload);

    /**
     * Get permission list by user id response body bean.
     *
     * @param userId the user id
     * @return the response body bean
     */
    @GetMapping("/permission-remote-api/permissions/{userId}")
    ResponseBodyBean<GetPermissionListByUserIdResponse> getPermissionListByUserId(@PathVariable Long userId);
}
