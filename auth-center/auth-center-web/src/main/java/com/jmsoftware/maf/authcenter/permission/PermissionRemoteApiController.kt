package com.jmsoftware.maf.authcenter.permission

import com.jmsoftware.maf.authcenter.permission.service.PermissionService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * <h1>PermissionRemoteApiController</h1>
 *
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/11/20 8:24 AM
 */
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
