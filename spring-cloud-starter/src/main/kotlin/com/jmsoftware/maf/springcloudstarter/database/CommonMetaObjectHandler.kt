package com.jmsoftware.maf.springcloudstarter.database

import cn.hutool.core.util.ObjectUtil
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
import com.jmsoftware.maf.common.domain.DeletedField
import com.jmsoftware.maf.common.util.Slf4j
import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import com.jmsoftware.maf.springcloudstarter.util.currentUsername
import org.apache.ibatis.reflection.MetaObject
import java.time.LocalDateTime

/**
 * # CommonMetaObjectHandler
 *
 * **CommonMetaObjectHandler** will inject these fields automatically when executing **INSERT** or **UPDATE**.
 *
 * This class is cooperating with the annotation `@TableField`. So the persistence Java class must be
 * annotated by `@TableField(value = COL_CREATED_BY, fill = INSERT)` or `@TableField(value =
 * COL_MODIFIED_BY, fill = UPDATE)`.
 *
 * | MySQL Field Name | Field Name Java |
 * | ---------------- | :-------------- |
 * | created_by       | createdBy       |
 * | created_time     | createdTime     |
 * | modified_by      | modifiedBy      |
 * | modified_time    | modifiedTime    |
 * | deleted          | deleted         |
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 11:31 AM
 * @see TableField
 */
@Slf4j
class CommonMetaObjectHandler : MetaObjectHandler {
    companion object {
        /**
         * The Java persistence object field name: createdBy
         */
        const val CREATED_BY_FIELD_NAME = "createdBy"

        /**
         * The Java persistence object field name: createdTime
         */
        const val CREATED_TIME_FIELD_NAME = "createdTime"

        /**
         * The Java persistence object field name: modifiedBy
         */
        const val MODIFIED_BY_FIELD_NAME = "modifiedBy"

        /**
         * The Java persistence object field name: modifiedTime
         */
        const val MODIFIED_TIME_FIELD_NAME = "modifiedTime"

        /**
         * The Java persistence object field name: deleted
         */
        const val DELETED_FIELD_NAME = "deleted"
        const val DEFAULT_USERNAME = "system"
    }

    override fun insertFill(metaObject: MetaObject) {
        // 严格填充,只针对非主键的字段,只有该表注解了fill 并且 字段名和字段属性 能匹配到才会进行填充(null 值不填充)
        log.atDebug().log { "Starting to insert fill metaObject: ${metaObject.originalObject}" }
        val currentUsername = currentUsername()
        if (ObjectUtil.isNull(currentUsername)) {
            log.warn("Current user's ID is null, which may cause the record in database is incorrect. This will happen when the request is ignored by gateway. Field: $CREATED_BY_FIELD_NAME")
            this.strictInsertFill(metaObject, CREATED_BY_FIELD_NAME, String::class.java, DEFAULT_USERNAME)
        } else {
            this.strictInsertFill(metaObject, CREATED_BY_FIELD_NAME, String::class.java, currentUsername)
        }
        this.strictInsertFill(metaObject, CREATED_TIME_FIELD_NAME, LocalDateTime::class.java, LocalDateTime.now())
            .strictInsertFill(metaObject, DELETED_FIELD_NAME, String::class.java, DeletedField.NOT_DELETED.value)
        log.atDebug().log { "Finished to insert fill metaObject: ${metaObject.originalObject}" }
    }

    override fun updateFill(metaObject: MetaObject) {
        log.atDebug().log { "Starting to update fill metaObject: ${metaObject.originalObject}" }
        val currentUsername = currentUsername()
        if (ObjectUtil.isNull(currentUsername)) {
            log.warn("Current user's ID is null, which may cause the record in database is incorrect. This will happen when the request is ignored by gateway. Field: $MODIFIED_BY_FIELD_NAME")
            this.strictUpdateFill(metaObject, CREATED_BY_FIELD_NAME, String::class.java, DEFAULT_USERNAME)
        } else {
            this.strictUpdateFill(metaObject, MODIFIED_BY_FIELD_NAME, String::class.java, currentUsername)
        }
        this.strictUpdateFill(metaObject, MODIFIED_TIME_FIELD_NAME, LocalDateTime::class.java, LocalDateTime.now())
        log.atDebug().log { "Finished to update fill metaObject: ${metaObject.originalObject}" }
    }
}
