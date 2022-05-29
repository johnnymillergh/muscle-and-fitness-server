package com.jmsoftware.maf.springcloudstarter.database

import com.baomidou.mybatisplus.annotation.*
import com.jmsoftware.maf.common.domain.DeletedField
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
     *
     * @see DeletedField
     */
    @TableLogic
    @TableField(value = COL_DELETED, fill = FieldFill.INSERT)
    var deleted: String = DeletedField.NOT_DELETED.value
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

    override fun toString(): String {
        return "BasePersistenceEntity(id=$id, createdBy=$createdBy, createdTime=$createdTime, modifiedBy=$modifiedBy, modifiedTime=$modifiedTime, version=$version, deleted='$deleted')"
    }
}
