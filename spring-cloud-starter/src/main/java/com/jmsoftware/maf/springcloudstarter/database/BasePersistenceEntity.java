package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.UPDATE;

/**
 * Description: BasePersistenceEntity, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 3/23/22 8:00 AM
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
