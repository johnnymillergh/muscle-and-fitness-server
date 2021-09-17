package com.jmsoftware.maf.springcloudstarter.database;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jmsoftware.maf.common.domain.DeletedField;
import com.jmsoftware.maf.springcloudstarter.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * <h1>CommonMetaObjectHandler</h1>
 * <p><strong>CommonMetaObjectHandler</strong> will inject these fields automatically when executing
 * <strong>INSERT</strong> or
 * <strong>UPDATE</strong>.</p>
 * <p>This class is cooperating with the annotation <code>@TableField</code>. So the persistence Java class must be
 * annotated by <code>@TableField(value = COL_CREATED_BY, fill = INSERT)</code> or <code>@TableField(value =
 * COL_MODIFIED_BY, fill = UPDATE)</code>.</p>
 * <figure><table>
 * <thead>
 * <tr><th>MySQL Field Name</th><th>Field Name Java</th></tr></thead>
 * <tbody><tr><td>created_by</td><td>createdBy</td></tr><tr><td>created_time</td><td>createdTime</td></tr><tr><td
 * >modified_by</td><td>modifiedBy</td></tr><tr><td>modified_time</td><td>modifiedTime</td></tr><tr><td>deleted</td
 * ><td>deleted</td></tr></tbody>
 * </table></figure>
 * <p>&nbsp;</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/23/2021 10:43 AM
 * @see TableField
 **/
@Slf4j
public class CommonMetaObjectHandler implements MetaObjectHandler {
    /**
     * The Java persistence object field name: createdBy
     */
    public static final String CREATED_BY_FIELD_NAME = "createdBy";
    /**
     * The Java persistence object field name: createdTime
     */
    public static final String CREATED_TIME_FIELD_NAME = "createdTime";
    /**
     * The Java persistence object field name: modifiedBy
     */
    public static final String MODIFIED_BY_FIELD_NAME = "modifiedBy";
    /**
     * The Java persistence object field name: modifiedTime
     */
    public static final String MODIFIED_TIME_FIELD_NAME = "modifiedTime";
    /**
     * The Java persistence object field name: deleted
     */
    public static final String DELETED_FIELD_NAME = "deleted";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 严格填充,只针对非主键的字段,只有该表注解了fill 并且 字段名和字段属性 能匹配到才会进行填充(null 值不填充)
        if (log.isDebugEnabled()) {
            log.debug("Starting to insert fill metaObject: {}", metaObject.getOriginalObject());
        }
        val currentId = UserUtil.getCurrentId();
        if (ObjectUtil.isNull(currentId)) {
            log.warn(
                    "Current user's ID is null, which may cause the record in database is incorrect. This will happen" +
                            " when the request is ignored by gateway. Field: {}", CREATED_BY_FIELD_NAME);
        } else {
            this.strictInsertFill(metaObject, CREATED_BY_FIELD_NAME, Long.class, currentId);
        }
        this.strictInsertFill(metaObject, CREATED_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now())
                .strictInsertFill(metaObject, DELETED_FIELD_NAME, Byte.class, DeletedField.NOT_DELETED.getValue());
        if (log.isDebugEnabled()) {
            log.debug("Finished to insert fill metaObject: {}", metaObject.getOriginalObject());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.debug("Starting to update fill metaObject: {}", metaObject.getOriginalObject());
        }
        val currentId = UserUtil.getCurrentId();
        if (ObjectUtil.isNull(currentId)) {
            log.warn(
                    "Current user's ID is null, which may cause the record in database is incorrect. This will happen" +
                            " when the request is ignored by gateway. Field: {}", MODIFIED_BY_FIELD_NAME);
        } else {
            this.strictUpdateFill(metaObject, MODIFIED_BY_FIELD_NAME, Long.class, currentId);
        }
        this.strictUpdateFill(metaObject, MODIFIED_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
        if (log.isDebugEnabled()) {
            log.debug("Finished to update fill metaObject: {}", metaObject.getOriginalObject());
        }
    }
}
