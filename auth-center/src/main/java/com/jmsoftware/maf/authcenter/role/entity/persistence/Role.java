package com.jmsoftware.maf.authcenter.role.entity.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <h1>Role</h1>
 * <p>
 * Role Persistence object class
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 3:22 PM
 */
@Data
@TableName(value = Role.TABLE_NAME)
public class Role {
    /**
     * Primary key
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    private Long id;

    /**
     * Role name
     */
    @TableField(value = COL_NAME)
    private String name;

    /**
     * Role description
     */
    @TableField(value = COL_DESCRIPTION)
    private String description;

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
     * Deleted flag.
     */
    @TableField(value = COL_DELETED)
    private Byte deleted;

    public static final String TABLE_NAME = "role";

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_CREATED_BY = "created_by";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_MODIFIED_BY = "modified_by";

    public static final String COL_MODIFIED_TIME = "modified_time";

    public static final String COL_DELETED = "deleted";
}
