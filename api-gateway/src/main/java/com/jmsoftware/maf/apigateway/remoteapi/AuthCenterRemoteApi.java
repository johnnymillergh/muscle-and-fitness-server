package com.jmsoftware.maf.apigateway.remoteapi;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtPayload;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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

    /**
     * Get permission list by user id response body bean.
     *
     * @param userId the user id
     * @return the response body bean
     */
    @GetMapping("/permission-remote-api/permissions/{userId}")
    Mono<ResponseBodyBean<GetPermissionListByUserIdResponse>> getPermissionListByUserId(@PathVariable Long userId);

    /**
     * Get permission list by role id list
     *
     * @param roleIdList the role id list
     * @return the response body bean
     */
    @RequestMapping(value = "/permission-remote-api/permissions", method = GET)
    Mono<ResponseBodyBean<GetPermissionListByRoleIdListResponse>> getPermissionListByRoleIdList(
            @Valid @RequestParam("roleIdList") List<@NotNull Long> roleIdList);

    /**
     * Parse mono.
     *
     * @param payload the payload
     * @return the mono
     */
    @PostMapping("/jwt-remote-api/parse")
    Mono<ResponseBodyBean<ParseJwtResponse>> parse(@Valid @RequestBody ParseJwtPayload payload);
}
