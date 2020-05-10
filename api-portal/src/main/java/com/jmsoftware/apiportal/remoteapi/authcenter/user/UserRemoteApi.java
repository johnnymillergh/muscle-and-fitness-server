package com.jmsoftware.apiportal.remoteapi.authcenter.user;

import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenPayload;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <h1>UserRemoteApi</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/10/20 4:50 PM
 */
@FeignClient(name = "auth-center")
public interface UserRemoteApi {
    /**
     * Gets user by login token.
     *
     * @param payload the payload
     * @return the user by login token
     */
    @PostMapping("/user/get-user-by-login-token")
    ResponseBodyBean<GetUserByLoginTokenResponse> getUserByLoginToken(@RequestBody GetUserByLoginTokenPayload payload);
}
