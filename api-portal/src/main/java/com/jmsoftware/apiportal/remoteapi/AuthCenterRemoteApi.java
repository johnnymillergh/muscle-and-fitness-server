package com.jmsoftware.apiportal.remoteapi;

import com.jmsoftware.apiportal.universal.aspect.ValidateArgument;
import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenPayload;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import com.jmsoftware.common.domain.authcenter.user.SaveUserForRegisteringPayload;
import com.jmsoftware.common.domain.authcenter.user.SaveUserForRegisteringResponse;
import org.springframework.cloud.openfeign.FeignClient;
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
@FeignClient(name = "auth-center")
public interface AuthCenterRemoteApi {
    /**
     * Gets user by login token.
     *
     * @param payload the payload
     * @return the user by login token
     */
    @ValidateArgument
    @PostMapping("/user-remote-api/get-user-by-login-token")
    ResponseBodyBean<GetUserByLoginTokenResponse> getUserByLoginToken(@Valid @RequestBody GetUserByLoginTokenPayload payload);

    /**
     * Gets role list by user id.
     *
     * @param payload the payload
     * @return the role list by user id
     */
    @ValidateArgument
    @PostMapping("/role-remote-api/get-role-list-by-user-id")
    ResponseBodyBean<GetRoleListByUserIdResponse> getRoleListByUserId(@Valid @RequestBody GetRoleListByUserIdPayload payload);

    /**
     * Save user for registering response body bean.
     *
     * @param payload the payload
     * @return the response body bean
     */
    @ValidateArgument
    @PostMapping("/user-remote-api/save-user-for-registering")
    ResponseBodyBean<SaveUserForRegisteringResponse> saveUserForRegistering(@Valid @RequestBody SaveUserForRegisteringPayload payload);

    /**
     * Gets permission list by role id list.
     *
     * @param payload the payload
     * @return the permission list by role id list
     */
    @ValidateArgument
    @PostMapping("/permission-remote-api/get-permission-list-by-role-id-list")
    ResponseBodyBean<GetPermissionListByRoleIdListResponse> getPermissionListByRoleIdList(@Valid @RequestBody GetPermissionListByRoleIdListPayload payload);

    /**
     * Get permission list by user id response body bean.
     *
     * @param payload the payload
     * @return the response body bean
     */
    @ValidateArgument
    @PostMapping("/permission-remote-api/get-permission-list-by-user-id")
    ResponseBodyBean<GetPermissionListByUserIdResponse> getPermissionListByUserId(@Valid @RequestBody GetPermissionListByUserIdPayload payload);
}
