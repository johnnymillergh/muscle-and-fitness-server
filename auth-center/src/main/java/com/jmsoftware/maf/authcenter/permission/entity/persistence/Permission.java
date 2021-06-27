package com.jmsoftware.maf.authcenter.permission.entity.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <h1>Permission</h1>
 * <p>
 * Permission Persistence object class
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 3:22 PM
 */
@Data
@TableName(value = Permission.TABLE_NAME)
public class Permission {
    /**
     * Primary key
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    private Long id;

    /**
     * URL. If type of record is page (1), URL stands for route; if type of record is button (2), URL stands for API
     */
    @TableField(value = COL_URL)
    private String url;

    /**
     * Permission description
     */
    @TableField(value = COL_DESCRIPTION)
    private String description;

    /**
     * Permission type. 1 - page; 2 - button
     */
    @TableField(value = COL_TYPE)
    private Byte type;

    /**
     * Permission expression
     */
    @TableField(value = COL_PERMISSION_EXPRESSION)
    private String permissionExpression;

    /**
     * HTTP method of API
     */
    @TableField(value = COL_METHOD)
    private Object method;

    /**
     * Sort number
     */
    @TableField(value = COL_SORT)
    private Integer sort;

    /**
     * Primary key of parent
     */
    @TableField(value = COL_PARENT_ID)
    private Long parentId;

    /**
     * Created by
     */
    @TableField(value = COL_CREATED_BY)
    private Long createdBy;

    /**
     * Created time
     */
    @TableField(value = COL_CREATED_TIME)
    private Date createdTime;

    /**
     * Modified by
     */
    @TableField(value = COL_MODIFIED_BY)
    private Long modifiedBy;

    /**
     * Modified time
     */
    @TableField(value = COL_MODIFIED_TIME)
    private Date modifiedTime;

    /**
     * Deleted flag
     */
    @TableField(value = COL_DELETED)
    private Byte deleted;

    public static final String TABLE_NAME = "permission";

    public static final String COL_ID = "id";

    public static final String COL_URL = "url";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_TYPE = "type";

    public static final String COL_PERMISSION_EXPRESSION = "permission_expression";

    public static final String COL_METHOD = "method";

    public static final String COL_SORT = "sort";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_MODIFIED_BY = "modified_by";

    public static final String COL_MODIFIED_TIME = "modified_time";

    public static final String COL_DELETED = "deleted";
}
