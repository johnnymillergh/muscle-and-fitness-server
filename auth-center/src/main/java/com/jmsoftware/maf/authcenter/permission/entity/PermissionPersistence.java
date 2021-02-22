package com.jmsoftware.maf.authcenter.permission.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.jmsoftware.maf.common.domain.DeletedField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Permission.(Permission) Persistence object class
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@Data
@TableName(value = "permission")
public class PermissionPersistence implements Serializable {
    private static final long serialVersionUID = -56601096713236790L;

    /**
     * Primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * URL. If type of record is page (1), URL stands for route; if type of record is button (2), URL stands for API
     */
    @TableField(value = "url")
    private String url;

    /**
     * Permission description
     */
    @TableField(value = "description")
    private String description;

    /**
     * Permission type. 1 - page; 2 - button
     */
    @TableField(value = "`type`")
    private Byte type;

    /**
     * Permission expression
     */
    @TableField(value = "permission_expression")
    private String permissionExpression;

    /**
     * HTTP method of API
     */
    @TableField(value = "`method`")
    private Object method;

    /**
     * Sort number
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * Primary key of parent
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * Deleted flag
     *
     * @see DeletedField
     */
    @TableLogic
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Byte deleted;

    /**
     * Created time
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * Modified time
     */
    @TableField(value = "modified_time", fill = FieldFill.INSERT_UPDATE)
    private Date modifiedTime;

    public static final String COL_ID = "id";

    public static final String COL_URL = "url";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_TYPE = "type";

    public static final String COL_PERMISSION_EXPRESSION = "permission_expression";

    public static final String COL_METHOD = "method";

    public static final String COL_SORT = "sort";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_DELETED = "deleted";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_MODIFIED_TIME = "modified_time";
}
