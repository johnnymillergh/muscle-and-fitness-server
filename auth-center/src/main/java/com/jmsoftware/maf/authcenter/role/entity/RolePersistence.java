package com.jmsoftware.maf.authcenter.role.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Role.(Role) Persistence object class
 *
 * @author Johnny Miller (锺俊)
 * @since 2020-05-10 22:39:45
 */
@Data
@TableName(value = "role")
public class RolePersistence implements Serializable {
    private static final long serialVersionUID = -81197803690669820L;
    /**
     * Primary key
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Role name
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * Role description
     */
    @TableField(value = "description")
    private String description;

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

    public static final String COL_NAME = "name";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_CREATED_TIME = "created_time";

    public static final String COL_MODIFIED_TIME = "modified_time";
}
