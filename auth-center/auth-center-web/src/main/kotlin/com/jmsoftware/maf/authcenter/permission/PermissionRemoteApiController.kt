package com.jmsoftware.maf.authcenter.permission

import com.jmsoftware.maf.authcenter.permission.service.PermissionService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * # PermissionRemoteApiController
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 8:54 PM
 */
@Validated
@RestController
@RequestMapping("/permission-remote-api")
class PermissionRemoteApiController(
    private val permissionService: PermissionService
) {
    @GetMapping("/permissions")
    fun getPermissionListByRoleIdList(payload: @Valid GetPermissionListByRoleIdListPayload)
            : ResponseBodyBean<GetPermissionListByRoleIdListResponse> =
        ResponseBodyBean.ofSuccess(permissionService.getPermissionListByRoleIdList(payload))
}
