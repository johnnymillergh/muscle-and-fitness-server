package com.jmsoftware.authcenter.role.service;

import com.jmsoftware.authcenter.role.entity.RolePersistence;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import lombok.NonNull;

import java.util.List;

/**
 * <h1>RoleService</h1>
 * <p>
 * Service of Role.(Role)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020 -05-10 22:39:49
 */
public interface RoleService {
    /**
     * Query by ID
     *
     * @param id the primary key ID
     * @return the entity
     */
    RolePersistence queryById(Long id);

    /**
     * Gets role list by user id.
     *
     * @param payload the payload
     * @return the role list by user id
     */
    GetRoleListByUserIdResponse getRoleListByUserId(GetRoleListByUserIdPayload payload);

    /**
     * Gets role list by user id.
     *
     * @param userId the user id
     * @return the role list by user id
     */
    List<RolePersistence> getRoleListByUserId(@NonNull Long userId);

    /**
     * Query all by limit
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the entity list
     */
    List<RolePersistence> queryAllByLimit(int offset, int limit);

    /**
     * Insert
     *
     * @param rolePersistence the entity
     * @return the entity
     */
    RolePersistence insert(RolePersistence rolePersistence);

    /**
     * Update
     *
     * @param rolePersistence the entity
     * @return the entity
     */
    RolePersistence update(RolePersistence rolePersistence);

    /**
     * Delete by ID
     *
     * @param id the primary key ID
     * @return the boolean
     */
    boolean deleteById(Long id);
}
