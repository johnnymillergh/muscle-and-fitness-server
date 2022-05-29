package com.jmsoftware.maf.authcenter.permission

import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse
import com.jmsoftware.maf.authcenter.permission.service.PermissionService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * # PermissionController
 *
 * Controller implementation of Permission.(Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 2:53 PM
 */
@RestController
class PermissionController(
    private val permissionService: PermissionService
) {
    @GetMapping("/permissions/services-info")
    fun getServicesInfo(): ResponseBodyBean<GetServicesInfoResponse> =
        ResponseBodyBean.ofSuccess(permissionService.getServicesInfo())
}

