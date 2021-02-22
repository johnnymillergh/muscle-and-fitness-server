package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jmsoftware.maf.common.domain.DeletedField;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

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
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {
    public final String DELETED_FIELD_NAME = "deleted";
    public final String CREATED_TIME_FIELD_NAME = "createdTime";
    public final String MODIFIED_TIME_FIELD_NAME = "modifiedTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Start to insert fill metaObject: {}", metaObject.getOriginalObject());
        val now = new Date();
        this.strictInsertFill(metaObject, DELETED_FIELD_NAME, Byte.class, DeletedField.NOT_DELETED.getValue());
        this.strictInsertFill(metaObject, CREATED_TIME_FIELD_NAME, Date.class, now);
        this.strictInsertFill(metaObject, MODIFIED_TIME_FIELD_NAME, Date.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Start to update fill metaObject: {}", metaObject.getOriginalObject());
        this.strictUpdateFill(metaObject, "modifiedTime", Date.class, new Date());
    }
}
