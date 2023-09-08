package com.jmsoftware.maf.authcenter.permission.persistence

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.jmsoftware.maf.springcloudstarter.database.BasePersistenceEntity

/**
 * # Permission
 *
 * Permission Persistence object class
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 4:19 PM
 */
@TableName(value = Permission.TABLE_NAME)
class Permission : BasePersistenceEntity() {
    companion object {
        const val TABLE_NAME = "permission"
        const val COL_URL = "url"
        const val COL_DESCRIPTION = "description"
        const val COL_TYPE = "type"
        const val COL_PERMISSION_EXPRESSION = "permission_expression"
        const val COL_METHOD = "method"
        const val COL_SORT = "`sort`"
        const val COL_PARENT_ID = "parent_id"
    }

    /**
     * URL. If type of record is page (1), URL stands for route; if type of record is button (2), URL stands for API
     */
    @TableField(value = COL_URL)
    lateinit var url: String

    /**
     * Permission description
     */
    @TableField(value = COL_DESCRIPTION)
    var description: String? = null

    /**
     * Permission type. 1 - page; 2 - button
     */
    @TableField(value = COL_TYPE)
    var type: Byte? = null

    /**
     * Permission expression
     */
    @TableField(value = COL_PERMISSION_EXPRESSION)
    var permissionExpression: String? = null

    /**
     * HTTP method of API
     */
    @TableField(value = COL_METHOD)
    var method: String? = null

    /**
     * Sort number
     */
    @TableField(value = COL_SORT)
    var sort: Int? = null

    /**
     * Primary key of parent
     */
    @TableField(value = COL_PARENT_ID)
    var parentId: Long? = null
}
