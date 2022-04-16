package com.jmsoftware.maf.common.domain.authcenter.permission

/**
 * # GetPermissionListByRoleIdListResponse
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 6:43 PM
 */
class GetPermissionListByRoleIdListResponse {
    /**
     * The Permission list.
     */
    var permissionList: MutableList<Permission> = mutableListOf()

    class Permission {
        /**
         * The Url.
         */
        var url: String? = null

        /**
         * The Type.
         */
        var type: Byte? = null

        /**
         * The Permission expression.
         */
        var permissionExpression: String? = null

        /**
         * The Method.
         */
        var method: String? = null
    }
}
