package com.jmsoftware.maf.authcenter.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.authcenter.permission.entity.GetServicesInfoResponse;
import com.jmsoftware.maf.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public interface PermissionService extends IService<PermissionPersistence> {
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
     * @param roleIdList the role id list
     * @return the permission list by role id list
     */
    List<PermissionPersistence> getPermissionListByRoleIdList(@NotEmpty List<Long> roleIdList);

    /**
     * Gets permission list by user id.
     *
     * @param userId the user id
     * @return the permission list by user id
     */
    GetPermissionListByUserIdResponse getPermissionListByUserId(@NotNull Long userId);

    /**
     * Gets permission list by user id.
     *
     * @param userId the user id
     * @return the permission list by user id
     */
    List<PermissionPersistence> getPermissionPersistenceListByUserId(@NotNull Long userId);

    /**
     * Gets services info.
     *
     * @return the services info
     */
    GetServicesInfoResponse getServicesInfo();
}
