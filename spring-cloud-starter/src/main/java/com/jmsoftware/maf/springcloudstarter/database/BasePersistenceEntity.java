/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.UPDATE;

/**
 * Description: BasePersistenceEntity, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 3/4/2022 11:38 PM
 **/
@Data
public class BasePersistenceEntity {
    public static final String COL_ID = "id";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_MODIFIED_BY = "modified_by";
    public static final String COL_MODIFIED_TIME = "modified_time";
    public static final String COL_VERSION = "version";
    public static final String COL_DELETED = "deleted";

    /**
     * Primary key
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    private Long id;
    /**
     * Created by
     */
    @TableField(value = COL_CREATED_BY, fill = INSERT)
    private String createdBy;
    /**
     * Created time
     */
    @TableField(value = COL_CREATED_TIME, fill = INSERT)
    private LocalDateTime createdTime;
    /**
     * Modified by
     */
    @TableField(value = COL_MODIFIED_BY, fill = UPDATE)
    private String modifiedBy;
    /**
     * Modified time
     */
    @TableField(value = COL_MODIFIED_TIME, fill = UPDATE)
    private LocalDateTime modifiedTime;
    /**
     * Optimistic locking
     */
    @Version
    @TableField(value = COL_VERSION)
    private Integer version;
    /**
     * Deleted. 'N' - not deleted; 'Y' - deleted
     */
    @TableLogic
    @TableField(value = COL_DELETED, fill = INSERT)
    private String deleted;
}
