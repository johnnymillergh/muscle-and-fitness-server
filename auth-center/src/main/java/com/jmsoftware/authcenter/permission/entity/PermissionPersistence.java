package com.jmsoftware.authcenter.permission.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Permission.(Permission) Persistence object class
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@Data
public class PermissionPersistence implements Serializable {
    private static final long serialVersionUID = -56601096713236790L;
    /**
    * Primary key
    */
    private Long id;
    /**
    * URL. If type of record is page (1), URL stands for route; if type of record is button (2), URL stands for API
    */
    private String url;
    /**
    * Permission description
    */
    private String description;
    /**
    * Permission type. 1 - page; 2 - button
    */
    private Integer type;
    /**
    * Permission expression
    */
    private String permissionExpression;
    /**
    * HTTP method of API
    */
    private String method;
    /**
    * Sort number
    */
    private Integer sort;
    /**
    * Primary key of parent
    */
    private Long parentId;
    /**
    * Deleted flag
    */
    private Integer deleted;
    /**
    * Created time
    */
    private Date createdTime;
    /**
    * Modified time
    */
    private Date modifiedTime;
}
