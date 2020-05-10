package com.jmsoftware.authcenter.universal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.authcenter.universal.domain.RolePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1>RoleMapper</h1>
 * <p>CRUD operations for table `t_role`</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:52
 **/
@Mapper
@Component
public interface RoleMapper extends BaseMapper<RolePO> {
    /**
     * Select role by user ID.
     *
     * @param userId User ID
     * @return Roles
     */
    List<RolePO> selectByUserId(Long userId);

    /**
     * Select page list
     *
     * @param page page object
     * @return role page list
     */
    List<RolePO> selectPageList(Page page);

    /**
     * Check role name's uniqueness
     * <p>
     * If id is null, then check for creating role's name; otherwise, check for created role's name
     *
     * @param po persistence object
     * @return the occurrence of the name of role
     */
    Integer checkRoleName(RolePO po);

    /**
     * Insert role
     *
     * @param po persistence object
     * @return primary key
     */
    Long insertRole(RolePO po);

    /**
     * Select role by name
     *
     * @param name role name
     * @return role
     */
    RolePO selectRoleByName(String name);

    /**
     * Update role by ID
     *
     * @param po persistence object
     * @return affected row
     */
    int updateRoleById(RolePO po);

    /**
     * Selection role list for selection
     *
     * @param page pagination object
     * @return role page list
     */
    IPage<RolePO> selectRoleListForSelection(Page page);
}
