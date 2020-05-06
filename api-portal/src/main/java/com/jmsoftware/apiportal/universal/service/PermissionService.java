package com.jmsoftware.apiportal.universal.service;


import com.jmsoftware.apiportal.universal.domain.ApiStatus;
import com.jmsoftware.apiportal.universal.domain.GetApiListPLO;
import com.jmsoftware.apiportal.universal.domain.PermissionPO;

import java.util.List;

/**
 * <h1>PermissionService</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-05-10 20:45
 **/
public interface PermissionService {
    /**
     * Save permissionPO
     *
     * @param po permissionPO
     * @return true - successful operation; false - failed operation
     */
    boolean savePermission(PermissionPO po);

    /**
     * Select permission list by role id
     *
     * @param ids Role's id list
     * @return PermissionPO list
     */
    List<PermissionPO> selectByRoleIdList(List<Long> ids);

    /**
     * Check if API is in use by URL
     *
     * @param url URL
     * @return api status
     * @see ApiStatus
     */
    ApiStatus checkApiIsInUse(String url);

    /**
     * Find APIs by URL prefix.
     *
     * @param urlPrefix URL prefix
     * @return permissions
     */
    List<PermissionPO> selectApisByUrlPrefix(String urlPrefix);

    /**
     * Query API list
     *
     * @param plo payload object
     * @return API list
     */
    List<PermissionPO> queryApiList(GetApiListPLO plo);
}
