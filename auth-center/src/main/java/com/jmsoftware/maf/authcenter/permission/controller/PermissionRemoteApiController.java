package com.jmsoftware.maf.authcenter.permission.controller;

import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = {"Permission Remote API"})
public class PermissionRemoteApiController {
    private final PermissionService permissionService;

    @GetMapping("/permissions")
    @ApiOperation(value = "Get permission list by role id list", notes = "Get permission list by role id list (remote)")
    public ResponseBodyBean<GetPermissionListByRoleIdListResponse> getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload) {
        return ResponseBodyBean.ofSuccess(this.permissionService.getPermissionListByRoleIdList(payload));
    }
}
