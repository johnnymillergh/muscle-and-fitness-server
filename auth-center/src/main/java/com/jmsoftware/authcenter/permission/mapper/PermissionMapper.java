package com.jmsoftware.authcenter.permission.mapper;

import com.jmsoftware.authcenter.permission.entity.PermissionPersistence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <h1>PermissionMapper</h1>
 * <p>
 * Mapper of Permission.(Permission)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5 /11/20 8:34 AM
 */
@Mapper
public interface PermissionMapper {
    /**
     * Query by id permission persistence.
     *
     * @param id the id
     * @return the permission persistence
     */
    PermissionPersistence queryById(Long id);

    /**
     * Query all by limit list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    List<PermissionPersistence> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * Query all list.
     *
     * @param permissionPersistence the permission persistence
     * @return the list
     */
    List<PermissionPersistence> queryAll(PermissionPersistence permissionPersistence);

    /**
     * Insert int.
     *
     * @param permissionPersistence the permission persistence
     * @return the int
     */
    int insert(PermissionPersistence permissionPersistence);

    /**
     * Update int.
     *
     * @param permissionPersistence the permission persistence
     * @return the int
     */
    int update(PermissionPersistence permissionPersistence);

    /**
     * Delete by id int.
     *
     * @param id the id
     * @return the int
     */
    int deleteById(Long id);

    /**
     * Select permission list by role id list list.
     *
     * @param roleIdList the role id list
     * @return the list
     */
    List<PermissionPersistence> selectPermissionListByRoleIdList(List<Long> roleIdList);

    /**
     * Select permission list by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<PermissionPersistence> selectPermissionListByUserId(Long userId);
}
