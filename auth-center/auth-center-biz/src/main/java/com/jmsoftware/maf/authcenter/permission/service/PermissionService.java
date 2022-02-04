package com.jmsoftware.maf.authcenter.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.authcenter.permission.response.GetServicesInfoResponse;
import com.jmsoftware.maf.authcenter.permission.persistence.Permission;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import com.jmsoftware.maf.common.exception.BizException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <h1>PermissionService</h1>
 * <p>
 * Service of Permission.(Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5 /11/20 8:34 AM
 */
@Validated
public interface PermissionService extends IService<Permission> {
    /**
     * Gets permission list by role id list.
     *
     * @param payload the payload
     * @return the permission list by role id list
     */
    GetPermissionListByRoleIdListResponse getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload);

    /**
     * Gets permission list by role id list.
     *
     * @param roleIdList         the role id list
     * @param permissionTypeList the permission type list
     * @return the permission list by role id list
     */
    List<Permission> getPermissionListByRoleIdList(@NotEmpty List<Long> roleIdList,
                                                   @NotEmpty List<PermissionType> permissionTypeList);

    /**
     * Gets services info.
     *
     * @return the services info
     * @throws BizException the business exception
     */
    GetServicesInfoResponse getServicesInfo() throws BizException;
}
