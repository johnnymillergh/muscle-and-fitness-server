/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
package com.jmsoftware.maf.authcenter.permission.service;

import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Description: PermissionServiceImpl, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/18/2022 11:22 PM
 **/
@Validated
public interface PermissionService {
    /**
     * Gets permission list by role id list.
     *
     * @param payload the payload
     * @return the permission list by role id list
     */
    GetPermissionListByRoleIdListResponse getPermissionListByRoleIdList(@Valid @NotNull GetPermissionListByRoleIdListPayload payload);

    /**
     * Gets services info.
     *
     * @return the services info
     */
    GetServicesInfoResponse getServicesInfo();
}
