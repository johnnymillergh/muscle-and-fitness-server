package com.jmsoftware.maf.authcenter.role.mapper;

import com.jmsoftware.maf.authcenter.role.entity.RolePersistence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <h1>RoleMapper</h1>
 * <p>
 * Mapper of Role.(Role)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020 -05-10 22:39:48
 */
@Mapper
public interface RoleMapper {
    /**
     * Query by id role.
     *
     * @param id the id
     * @return the role
     */
    RolePersistence queryById(Long id);

    /**
     * Query all by limit list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    List<RolePersistence> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * Query all list.
     *
     * @param rolePersistence the role
     * @return the list
     */
    List<RolePersistence> queryAll(RolePersistence rolePersistence);

    /**
     * Insert int.
     *
     * @param rolePersistence the role
     * @return the int
     */
    int insert(RolePersistence rolePersistence);

    /**
     * Update int.
     *
     * @param rolePersistence the role
     * @return the int
     */
    int update(RolePersistence rolePersistence);

    /**
     * Delete by id int.
     *
     * @param id the id
     * @return the int
     */
    int deleteById(Long id);

    /**
     * Select role list by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<RolePersistence> selectRoleListByUserId(Long userId);
}
