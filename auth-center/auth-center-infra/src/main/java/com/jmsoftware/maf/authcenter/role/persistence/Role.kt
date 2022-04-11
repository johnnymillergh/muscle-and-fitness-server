package com.jmsoftware.maf.authcenter.role.persistence

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.jmsoftware.maf.springcloudstarter.database.BasePersistenceEntity

/**
 * # Role
 *
 * Role Persistence object class
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/10/22 7:15 PM
 */
@TableName(value = Role.TABLE_NAME)
class Role(
    /**
     * Role name
     */
    @TableField(value = COL_NAME)
    val name: String,
    /**
     * Role description
     */
    @TableField(value = COL_DESCRIPTION)
    val description: String
) : BasePersistenceEntity() {
    companion object {
        const val TABLE_NAME = "role"
        const val COL_NAME = "name"
        const val COL_DESCRIPTION = "description"
    }
}
