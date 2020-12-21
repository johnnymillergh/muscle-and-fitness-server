package com.jmsoftware.maf.authcenter.permission.remote;

import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = {"Permission Remote API"})
public class PermissionRemoteApiController {
    private final PermissionService permissionService;

    @GetMapping("/permissions")
    @ApiOperation(value = "Get permission list by role id list", notes = "Get permission list by role id list (remote)")
    public ResponseBodyBean<GetPermissionListByRoleIdListResponse> getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload) {
        return ResponseBodyBean.ofSuccess(permissionService.getPermissionListByRoleIdList(payload));
    }

    @PostMapping("/permissions/{userId}")
    @ApiOperation(value = "Get permission list by user id", notes = "Get permission list by user id")
    public ResponseBodyBean<GetPermissionListByUserIdResponse> getPermissionListByUserId(@PathVariable Long userId) {
        return ResponseBodyBean.ofSuccess(permissionService.getPermissionListByUserId(userId));
    }
}
