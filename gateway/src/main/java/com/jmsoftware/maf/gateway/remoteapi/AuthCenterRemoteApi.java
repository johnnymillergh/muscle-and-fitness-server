package com.jmsoftware.maf.gateway.remoteapi;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * <h1>AuthCenterRemoteApi</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/10/20 4:50 PM
 */
@Validated
@ReactiveFeignClient(name = "auth-center")
public interface AuthCenterRemoteApi {
    /**
     * Gets user by login token.
     *
     * @param loginToken the login token, e.q. username, email or phone number
     * @return the user by login token
     */
    @GetMapping("/user-remote-api/users/{loginToken}")
    Mono<ResponseBodyBean<GetUserByLoginTokenResponse>> getUserByLoginToken(@PathVariable String loginToken);

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    @GetMapping("/role-remote-api/roles/{userId}")
    Mono<ResponseBodyBean<GetRoleListByUserIdResponse>> getRoleListByUserId(@PathVariable Long userId);
}
