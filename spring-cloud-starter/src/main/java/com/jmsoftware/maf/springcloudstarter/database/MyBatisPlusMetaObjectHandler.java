package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jmsoftware.maf.common.domain.DeletedField;
import com.jmsoftware.maf.springcloudstarter.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * <h1>MyBatisPlusConfiguration</h1>
 * <p>
 * Change description here.
 * <p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-05-02 11:57
 **/
@Slf4j
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {
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
        log.info("Starting to insert fill metaObject: {}", metaObject.getOriginalObject());
        this.strictInsertFill(metaObject, CREATED_BY_FIELD_NAME, Long.class, UserUtil.getCurrentId())
                .strictInsertFill(metaObject, CREATED_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now())
                .strictInsertFill(metaObject, DELETED_FIELD_NAME, Byte.class, DeletedField.NOT_DELETED.getValue());
        log.info("Finished to insert fill metaObject: {}", metaObject.getOriginalObject());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Starting to update fill metaObject: {}", metaObject.getOriginalObject());
        this.strictUpdateFill(metaObject, MODIFIED_BY_FIELD_NAME, Long.class, UserUtil.getCurrentId())
                .strictUpdateFill(metaObject, MODIFIED_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
        log.info("Finished to update fill metaObject: {}", metaObject.getOriginalObject());
    }
}
