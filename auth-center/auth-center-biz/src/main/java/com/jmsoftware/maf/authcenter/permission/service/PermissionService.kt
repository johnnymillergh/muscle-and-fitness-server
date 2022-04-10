package com.jmsoftware.maf.authcenter.permission.service

import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 * Description: PermissionServiceImpl, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/18/2022 11:22 PM
 **/
@Validated
interface PermissionService {
    /**
     * Gets permission list by role id list.
     *
     * @param payload the payload
     * @return the permission list by role id list
     */
    fun getPermissionListByRoleIdList(
        payload: @Valid @NotNull GetPermissionListByRoleIdListPayload
    ): GetPermissionListByRoleIdListResponse

    /**
     * Gets services info.
     *
     * @return the services info
     */
    fun getServicesInfo(): GetServicesInfoResponse
}
