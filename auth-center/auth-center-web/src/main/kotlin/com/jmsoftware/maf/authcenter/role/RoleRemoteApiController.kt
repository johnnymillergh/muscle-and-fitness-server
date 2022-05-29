package com.jmsoftware.maf.authcenter.role

import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * # RoleRemoteApiController
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 3:08 PM
 */
@RestController
@RequestMapping("/role-remote-api")
class RoleRemoteApiController(
    private val roleDomainService: RoleDomainService
) {
    @GetMapping("/roles/{userId}")
    fun getRoleList(@PathVariable userId: Long): ResponseBodyBean<GetRoleListByUserIdResponse> =
        ResponseBodyBean.ofSuccess(roleDomainService.getRoleList(userId))
}
