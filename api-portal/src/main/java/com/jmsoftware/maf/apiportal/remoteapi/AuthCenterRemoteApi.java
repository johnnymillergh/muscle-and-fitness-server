package com.jmsoftware.maf.apiportal.remoteapi;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdPayload;
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
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
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
     * @param payload the payload
     * @return the role list by user id
     */
    @PostMapping("/role-remote-api/get-role-list-by-user-id")
    ResponseBodyBean<GetRoleListByUserIdResponse> getRoleListByUserId(@Valid @RequestBody GetRoleListByUserIdPayload payload);

    /**
     * Save user for registering response body bean.
     *
     * @param payload the payload
     * @return the response body bean
     */
    @PostMapping("/user-remote-api/save-user-for-register")
    ResponseBodyBean<SaveUserForRegisteringResponse> saveUserForRegistering(@Valid @RequestBody SaveUserForRegisteringPayload payload);

    /**
     * Gets permission list by role id list.
     *
     * @param payload the payload
     * @return the permission list by role id list
     */
    @PostMapping("/permission-remote-api/get-permission-list-by-role-id-list")
    ResponseBodyBean<GetPermissionListByRoleIdListResponse> getPermissionListByRoleIdList(@Valid @RequestBody GetPermissionListByRoleIdListPayload payload);

    /**
     * Get permission list by user id response body bean.
     *
     * @param payload the payload
     * @return the response body bean
     */
    @PostMapping("/permission-remote-api/get-permission-list-by-user-id")
    ResponseBodyBean<GetPermissionListByUserIdResponse> getPermissionListByUserId(@Valid @RequestBody GetPermissionListByUserIdPayload payload);
}
