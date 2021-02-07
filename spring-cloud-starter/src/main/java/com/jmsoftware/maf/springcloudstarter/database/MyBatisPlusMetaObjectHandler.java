package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jmsoftware.maf.common.domain.DeleteFlag;
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
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Start to insert fill ....");
        val now = new Date();
        this.strictInsertFill(metaObject, "deleted", Byte.class, DeleteFlag.NOT_DELETED.getValue());
        this.strictInsertFill(metaObject, "createdTime", Date.class, now);
        this.strictInsertFill(metaObject, "modifiedTime", Date.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Start to update fill ....");
        this.strictUpdateFill(metaObject, "modifiedTime", Date.class, new Date());
    }
}
