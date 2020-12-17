package com.jmsoftware.maf.authcenter.permission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmsoftware.maf.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * <h1>PermissionService</h1>
 * <p>
 * Service of Permission.(Permission)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
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
    List<PermissionPersistence> getPermissionListByRoleIdList(@NonNull List<Long> roleIdList);

    /**
     * Gets permission list by user id.
     *
     * @param payload the payload
     * @return the permission list by user id
     */
    GetPermissionListByUserIdResponse getPermissionListByUserId(@Valid GetPermissionListByUserIdPayload payload);

    /**
     * Gets permission list by user id.
     *
     * @param userId the user id
     * @return the permission list by user id
     */
    List<PermissionPersistence> getPermissionListByUserId(@NonNull Long userId);
}
