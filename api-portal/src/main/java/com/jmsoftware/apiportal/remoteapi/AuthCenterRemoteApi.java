package com.jmsoftware.apiportal.remoteapi;

import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenPayload;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/user-remote-api/get-user-by-login-token")
    ResponseBodyBean<GetUserByLoginTokenResponse> getUserByLoginToken(@RequestBody GetUserByLoginTokenPayload payload);

    /**
     * Gets role list by user id.
     *
     * @param payload the payload
     * @return the role list by user id
     */
    @PostMapping("/role-remote-api/get-role-list-by-user-id")
    ResponseBodyBean<GetRoleListByUserIdResponse> getRoleListByUserId(@RequestBody GetRoleListByUserIdPayload payload);
}
