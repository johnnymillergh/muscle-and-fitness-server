package com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jmsoftware.maf.springcloudstarter.database.BasePersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Quartz Job Configuration
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = QuartzJobConfiguration.TABLE_NAME)
public class QuartzJobConfiguration extends BasePersistenceEntity implements Serializable {
    private static final long serialVersionUID = -4561633114475541640L;

    public static final String TABLE_NAME = "quartz_job_configuration";
    public static final String COL_NAME = "name";
    public static final String COL_GROUP = "`group`";
    public static final String COL_SERVICE_NAME = "service_name";
    public static final String COL_INVOKE_TARGET = "invoke_target";
    public static final String COL_CRON_EXPRESSION = "cron_expression";
    public static final String COL_MISFIRE_POLICY = "misfire_policy";
    public static final String COL_CONCURRENT = "concurrent";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_STATUS = "status";

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
}
