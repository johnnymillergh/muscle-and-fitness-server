package com.jmsoftware.maf.authcenter.user.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * User-role Relation. Roles that users have.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/29/2021 12:09 PM
 */
@Data
@TableName(value = UserRole.TABLE_NAME)
public class UserRole {
    /**
     * The primary key
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    private Long id;

    /**
     * The primary key of user
     */
    @TableField(value = COL_USER_ID)
    private Long userId;

    /**
     * The primary key of role
     */
    @TableField(value = COL_ROLE_ID)
    private Long roleId;

    public static final String TABLE_NAME = "user_role";

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_ROLE_ID = "role_id";
}
