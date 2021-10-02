package com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;
import static com.baomidou.mybatisplus.annotation.FieldFill.UPDATE;

/**
 * Quartz Job Configuration
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Data
@TableName(value = QuartzJobConfiguration.TABLE_NAME)
public class QuartzJobConfiguration implements Serializable {
    public static final String TABLE_NAME = "quartz_job_configuration";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_GROUP = "`group`";
    public static final String COL_SERVICE_NAME = "service_name";
    public static final String COL_INVOKE_TARGET = "invoke_target";
    public static final String COL_CRON_EXPRESSION = "cron_expression";
    public static final String COL_MISFIRE_POLICY = "misfire_policy";
    public static final String COL_CONCURRENT = "concurrent";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_STATUS = "status";
    public static final String COL_CREATED_BY = "created_by";
    public static final String COL_CREATED_TIME = "created_time";
    public static final String COL_MODIFIED_BY = "modified_by";
    public static final String COL_MODIFIED_TIME = "modified_time";
    public static final String COL_DELETED = "deleted";
    private static final long serialVersionUID = -4561633114475541640L;
    /**
     * The primary key ID
     */
    @TableId(value = COL_ID, type = IdType.AUTO)
    private Long id;
    /**
     * Name of job
     */
    @TableField(value = COL_NAME)
    private String name;
    /**
     * Group of job
     */
    @TableField(value = COL_GROUP)
    private String group;
    /**
     * Service name, equal to artifact-id
     */
    @TableField(value = COL_SERVICE_NAME)
    private String serviceName;
    /**
     * Invoke target
     */
    @TableField(value = COL_INVOKE_TARGET)
    private String invokeTarget;
    /**
     * Cron Expressions
     */
    @TableField(value = COL_CRON_EXPRESSION)
    private String cronExpression;
    /**
     * Trigger's misfire policy
     */
    @TableField(value = COL_MISFIRE_POLICY)
    private Byte misfirePolicy;
    /**
     * Concurrent. true(1): is concurrent, false(0): not concurrent
     */
    @TableField(value = COL_CONCURRENT)
    private Byte concurrent;
    /**
     * Description
     */
    @TableField(value = COL_DESCRIPTION)
    private String description;
    /**
     * Status
     */
    @TableField(value = COL_STATUS)
    private Byte status;
    /**
     * Created by
     */
    @TableField(value = COL_CREATED_BY, fill = INSERT)
    private Long createdBy;
    /**
     * Craeted time
     */
    @TableField(value = COL_CREATED_TIME, fill = INSERT)
    private LocalDateTime createdTime;
    /**
     * Modified by
     */
    @TableField(value = COL_MODIFIED_BY, fill = UPDATE)
    private Long modifiedBy;
    /**
     * Modified time
     */
    @TableField(value = COL_MODIFIED_TIME, fill = UPDATE)
    private LocalDateTime modifiedTime;
    /**
     * Deleted
     */
    @TableField(value = COL_DELETED, fill = INSERT)
    private Byte deleted;
}
