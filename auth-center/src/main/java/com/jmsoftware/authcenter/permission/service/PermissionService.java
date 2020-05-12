package com.jmsoftware.authcenter.permission.service;

import com.jmsoftware.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import lombok.NonNull;

import java.util.List;

/**
 * <h1>PermissionService</h1>
 * <p>
 * Service of Permission.(Permission)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5 /11/20 8:34 AM
 */
public interface PermissionService {
    /**
     * Query by ID
     *
     * @param id the primary key ID
     * @return the entity
     */
    PermissionPersistence queryById(Long id);

    /**
     * Query all by limit
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the entity list
     */
    List<PermissionPersistence> queryAllByLimit(int offset, int limit);

    /**
     * Insert
     *
     * @param permissionPersistence the entity
     * @return the entity
     */
    PermissionPersistence insert(PermissionPersistence permissionPersistence);

    /**
     * Update
     *
     * @param permissionPersistence the entity
     * @return the entity
     */
    PermissionPersistence update(PermissionPersistence permissionPersistence);

    /**
     * Delete by ID
     *
     * @param id the primary key ID
     * @return the boolean
     */
    boolean deleteById(Long id);

    /**
     * Gets permission list by role id list.
     *
     * @param payload the payload
     * @return the permission list by role id list
     */
    GetPermissionListByRoleIdListResponse getPermissionListByRoleIdList(GetPermissionListByRoleIdListPayload payload);

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
    GetPermissionListByUserIdResponse getPermissionListByUserId(GetPermissionListByUserIdPayload payload);

    /**
     * Gets permission list by user id.
     *
     * @param userId the user id
     * @return the permission list by user id
     */
    List<PermissionPersistence> getPermissionListByUserId(@NonNull Long userId);
}
