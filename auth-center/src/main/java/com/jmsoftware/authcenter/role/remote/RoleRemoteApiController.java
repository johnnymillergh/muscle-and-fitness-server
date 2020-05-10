package com.jmsoftware.authcenter.role.remote;

import com.jmsoftware.authcenter.role.service.RoleService;
import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
@Api(tags = {"Role Remote API Controller"})
public class RoleRemoteApiController {
    private final RoleService roleService;

    @PostMapping("/get-role-list-by-user-id")
    public ResponseBodyBean<GetRoleListByUserIdResponse> getRoleListByUserId(@Valid @RequestBody GetRoleListByUserIdPayload payload) {
        return ResponseBodyBean.ofSuccess(roleService.getRoleListByUserId(payload));
    }
}
