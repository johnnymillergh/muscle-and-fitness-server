package com.jmsoftware.apiportal.universal.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.apiportal.universal.domain.RolePO;

import java.util.List;

/**
 * <h1>RoleService</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-05-18 12:03
 **/
public interface RoleService {
    /**
     * Get role list
     *
     * @param page page object
     * @return role list
     */
    List<RolePO> getList(Page page);

    /**
     * Check the uniqueness of name of role
     * <p>
     * If id is null, then check for creating role's name; otherwise, check for created role's name
     *
     * @param po persistence object
     * @return true - available; false - not available
     */
    boolean checkRoleName(RolePO po);

    /**
     * Insert a role
     *
     * @param po persistence object
     * @return true - insert successfully; false - insert failure
     */
    boolean insertRole(RolePO po);

    /**
     * Handle the name of role
     *
     * @param roleName the name of role
     * @return processed role name
     */
    String handleRoleName(String roleName);

    /**
     * Search role by name
     *
     * @param roleName role name
     * @return role
     */
    RolePO searchRole(String roleName);

    /**
     * Update role
     *
     * @param po persistence object
     * @return true - update successfully; false - update failure
     */
    boolean updateRole(RolePO po);

    /**
     * Get roles by user ID
     *
     * @param userId user ID
     * @return role list
     */
    List<RolePO> getRolesByUserId(Long userId);

    /**
     * Get role list for selection
     *
     * @param page page object
     * @return role list
     */
    List<RolePO> getListForSelection(Page page);
}
