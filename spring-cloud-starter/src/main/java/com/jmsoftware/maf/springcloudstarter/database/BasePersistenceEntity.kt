package com.jmsoftware.maf.springcloudstarter.database

import com.baomidou.mybatisplus.annotation.*
import java.time.LocalDateTime

/**
 * Description: BasePersistenceEntity, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 3/23/22 8:00 AM
 */
open class BasePersistenceEntity(
    /**
     * Primary key
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    var id: Long? = null,
    /**
     * Created by
     */
    @TableField(value = COL_CREATED_BY, fill = FieldFill.INSERT)
    var createdBy: String? = null,
    /**
     * Created time
     */
    @TableField(value = COL_CREATED_TIME, fill = FieldFill.INSERT)
    var createdTime: LocalDateTime? = null,
    /**
     * Modified by
     */
    @TableField(value = COL_MODIFIED_BY, fill = FieldFill.UPDATE)
    var modifiedBy: String? = null,
    /**
     * Modified time
     */
    @TableField(value = COL_MODIFIED_TIME, fill = FieldFill.UPDATE)
    var modifiedTime: LocalDateTime? = null,
    /**
     * Optimistic locking
     */
    @Version
    @TableField(value = COL_VERSION)
    var version: Int? = null,
    /**
     * Deleted. 'N' - not deleted; 'Y' - deleted
     */
    @TableLogic
    @TableField(value = COL_DELETED, fill = FieldFill.INSERT)
    var deleted: String? = null
) {
    companion object {
        const val COL_ID = "id"
        const val COL_CREATED_BY = "created_by"
        const val COL_CREATED_TIME = "created_time"
        const val COL_MODIFIED_BY = "modified_by"
        const val COL_MODIFIED_TIME = "modified_time"
        const val COL_VERSION = "version"
        const val COL_DELETED = "deleted"
    }
}
