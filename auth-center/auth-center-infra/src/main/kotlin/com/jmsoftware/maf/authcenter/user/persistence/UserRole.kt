package com.jmsoftware.maf.authcenter.user.persistence

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

/**
 * # UserRole
 *
 * User-role Relation. Roles that users have.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:51 PM
 */
@TableName(value = UserRole.TABLE_NAME)
class UserRole {
    companion object {
        const val TABLE_NAME = "user_role"
        const val COL_ID = "id"
        const val COL_USER_ID = "user_id"
        const val COL_ROLE_ID = "role_id"
    }

    /**
     * The primary key
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    var id: Long? = null

    /**
     * The primary key of user
     */
    @TableField(value = COL_USER_ID)
    var userId: Long? = null

    /**
     * The primary key of role
     */
    @TableField(value = COL_ROLE_ID)
    var roleId: Long? = null
}
