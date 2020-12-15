package com.jmsoftware.maf.authcenter.role.remote;

import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>RoleRemoteApiController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/10/20 10:43 PM
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/role-remote-api")
@Api(tags = {"Role Remote API"})
public class RoleRemoteApiController {
    private final RoleService roleService;

    @GetMapping("/roles/{userId}")
    @ApiOperation(value = "Get role list", notes = "Get role list (Remote API)")
    public ResponseBodyBean<GetRoleListByUserIdResponse> getRoleList(@PathVariable Long userId) {
        return ResponseBodyBean.ofSuccess(roleService.getRoleList(userId));
    }
}
