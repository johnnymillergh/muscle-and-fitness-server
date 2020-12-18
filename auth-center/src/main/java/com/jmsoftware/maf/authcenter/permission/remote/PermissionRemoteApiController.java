package com.jmsoftware.maf.authcenter.permission.remote;

import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/11/20 8:24 AM
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission-remote-api")
@Api(tags = {"Permission Remote API Controller"})
public class PermissionRemoteApiController {
    private final PermissionService permissionService;

    @PostMapping("/get-permission-list-by-role-id-list")
    @ApiOperation(value = "Get permission list by role id list", notes = "GGet permission list by role id list")
    public ResponseBodyBean<GetPermissionListByRoleIdListResponse> getPermissionListByRoleIdList(@Valid @RequestBody GetPermissionListByRoleIdListPayload payload) {
        return ResponseBodyBean.ofSuccess(permissionService.getPermissionListByRoleIdList(payload));
    }

    @PostMapping("/get-permission-list-by-user-id")
    @ApiOperation(value = "Get permission list by user id", notes = "Get permission list by user id")
    public ResponseBodyBean<GetPermissionListByUserIdResponse> getPermissionListByUserId(@Valid @RequestBody GetPermissionListByUserIdPayload payload) {
        return ResponseBodyBean.ofSuccess(permissionService.getPermissionListByUserId(payload));
    }
}
