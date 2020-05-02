package com.jmsoftware.common.domain;

import lombok.Data;

import java.util.Date;

/**
 * <h1>PermissionPo</h1>
 * <p>Persistence class for table `t_permission`</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 19:48
 **/
@Data
public class PermissionPo {
    /**
     * Primary key
     */
    private Long id;
    /**
     * URL. If type of record is page (1), URL stands for route; if type of record is button (2), URL stands for API
     */
    private String url;
    /**
     * PermissionPo description
     */
    private String description;
    /**
     * PermissionPo type. Page-1, Button-2
     */
    private Integer type;
    /**
     * PermissionPo expression.
     */
    private String permissionExpression;
    /**
     * HTTP method of API.
     */
    private String method;
    /**
     * Sort.
     */
    private Integer sort;
    /**
     * Primary key of parent.
     */
    private Long parentId;
    /**
     * Deleted flag
     */
    private Byte deleted;
    /**
     * Created time
     */
    private Date gmtCreated;
    /**
     * Modified time
     */
    private Date gmtModified;
}
