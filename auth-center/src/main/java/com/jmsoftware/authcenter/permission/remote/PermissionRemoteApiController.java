package com.jmsoftware.authcenter.permission.remote;

import com.jmsoftware.authcenter.permission.service.PermissionService;
import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>PermissionRemoteApiController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/11/20 8:24 AM
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission-remote-api")
@Api(tags = {"Permission Remote API Controller"})
public class PermissionRemoteApiController {
    private final PermissionService permissionService;

    @PostMapping("/get-permission-list-by-role-id-list")
    public ResponseBodyBean<GetPermissionListByRoleIdListResponse> getPermissionListByRoleIdList(@Valid @RequestBody GetPermissionListByRoleIdListPayload payload) {
        return ResponseBodyBean.ofSuccess(permissionService.getPermissionListByRoleIdList(payload));
    }
}
