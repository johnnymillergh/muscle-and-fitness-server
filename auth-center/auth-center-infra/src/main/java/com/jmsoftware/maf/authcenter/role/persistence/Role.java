package com.jmsoftware.maf.authcenter.role.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jmsoftware.maf.springcloudstarter.database.BasePersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>Role</h1>
 * <p>
 * Role Persistence object class
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 3:22 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = Role.TABLE_NAME)
public class Role extends BasePersistenceEntity {
    public static final String TABLE_NAME = "role";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
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
}
