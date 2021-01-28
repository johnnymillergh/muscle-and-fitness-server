package com.jmsoftware.maf.apigateway.remoteapi;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * <h1>AuthCenterRemoteApi</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/10/20 4:50 PM
 */
@Service
@Validated
public class AuthCenterRemoteApi {
    /**
     * Gets user by login token.
     *
     * @param loginToken the login token, e.q. username, email or phone number
     * @return the user by login token
     */
    @GetMapping("/user-remote-api/users/{loginToken}")
    Mono<ResponseBodyBean<GetUserByLoginTokenResponse>> getUserByLoginToken(@PathVariable String loginToken) {
        return null;
    }

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    @GetMapping("/role-remote-api/roles/{userId}")
    Mono<ResponseBodyBean<GetRoleListByUserIdResponse>> getRoleListByUserId(@PathVariable Long userId) {
        return null;
    }

    /**
     * Get permission list by role id list
     *
     * @param roleIdList         the role id list
     * @param permissionTypeList the permission type list
     * @return the response body bean
     */
    @RequestMapping(value = "/permission-remote-api/permissions", method = GET)
    Mono<ResponseBodyBean<GetPermissionListByRoleIdListResponse>> getPermissionListByRoleIdList(
            @Valid @RequestParam("roleIdList") List<@NotNull Long> roleIdList,
            @Valid @RequestParam("permissionTypeList") List<@NotNull PermissionType> permissionTypeList) {
        return null;
    }

    /**
     * Parse mono.
     *
     * @param headers the HTTP headers
     * @return the mono
     */
    @GetMapping("/jwt-remote-api/parse")
    Mono<ResponseBodyBean<ParseJwtResponse>> parse(@RequestHeader Map<String, String> headers) {
        return null;
    }
}
