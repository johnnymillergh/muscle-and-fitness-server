package com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.jmsoftware.maf.springcloudstarter.database.BasePersistenceEntity
import java.io.Serial
import java.io.Serializable

/**
 * # QuartzJobConfiguration
 *
 * Quartz Job Configuration
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:36 PM
 */
@TableName(value = QuartzJobConfiguration.TABLE_NAME)
class QuartzJobConfiguration : BasePersistenceEntity(), Serializable {
    companion object {
        @Serial
        var serialVersionUID = -4561633114475541640L
        const val TABLE_NAME = "quartz_job_configuration"
        const val COL_NAME = "name"
        const val COL_GROUP = "`group`"
        const val COL_SERVICE_NAME = "service_name"
        const val COL_INVOKE_TARGET = "invoke_target"
        const val COL_CRON_EXPRESSION = "cron_expression"
        const val COL_MISFIRE_POLICY = "misfire_policy"
        const val COL_CONCURRENT = "concurrent"
        const val COL_DESCRIPTION = "description"
        const val COL_STATUS = "status"
    }

    /**
     * Name of job
     */
    @TableField(value = COL_NAME)
    var name: String? = null

    /**
     * Group of job
     */
    @TableField(value = COL_GROUP)
    var group: String? = null

    /**
     * Service name, equal to artifact-id
     */
    @TableField(value = COL_SERVICE_NAME)
    var serviceName: String? = null

    /**
     * Invoke target
     */
    @TableField(value = COL_INVOKE_TARGET)
    var invokeTarget: String? = null

    /**
     * Cron Expressions
     */
    @TableField(value = COL_CRON_EXPRESSION)
    var cronExpression: String? = null

    /**
     * Trigger's misfire policy
     */
    @TableField(value = COL_MISFIRE_POLICY)
    var misfirePolicy: Byte? = null

    /**
     * Concurrent. true(1): is concurrent, false(0): not concurrent
     */
    @TableField(value = COL_CONCURRENT)
    var concurrent: Byte? = null

    /**
     * Description
     */
    @TableField(value = COL_DESCRIPTION)
    var description: String? = null

    /**
     * Status
     */
    @TableField(value = COL_STATUS)
    var status: Byte? = null
}
