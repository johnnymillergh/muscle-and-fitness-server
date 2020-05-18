package com.jmsoftware.maf.common.domain.authcenter.permission;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>GetPermissionListByRoleIdListResponse</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5 /11/20 8:38 AM
 */
@Data
public class GetPermissionListByRoleIdListResponse {
    /**
     * The Permission list.
     */
    private final List<Permission> permissionList = new LinkedList<>();

    @Data
    public static class Permission {
        /**
         * The Url.
         */
        private String url;
        /**
         * The Type.
         */
        private Integer type;
        /**
         * The Permission expression.
         */
        private String permissionExpression;
        /**
         * The Method.
         */
        private String method;
    }
}
