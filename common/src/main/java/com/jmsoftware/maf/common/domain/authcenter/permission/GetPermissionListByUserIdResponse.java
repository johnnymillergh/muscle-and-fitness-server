package com.jmsoftware.maf.common.domain.authcenter.permission;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>GetPermissionListByUserIdResponse</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/12/20 9:04 AM
 **/
@Data
public class GetPermissionListByUserIdResponse {
    /**
     * The Permission list.
     */
    private final List<GetPermissionListByUserIdResponse.Permission> permissionList = new LinkedList<>();

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
