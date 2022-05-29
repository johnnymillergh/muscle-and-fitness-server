package com.jmsoftware.maf.springcloudstarter.quartz.entity

import com.jmsoftware.maf.springcloudstarter.quartz.constant.Concurrent
import com.jmsoftware.maf.springcloudstarter.quartz.constant.Concurrent.DISALLOW_CONCURRENT
import com.jmsoftware.maf.springcloudstarter.quartz.constant.MisfirePolicy
import com.jmsoftware.maf.springcloudstarter.quartz.constant.MisfirePolicy.MISFIRE_INSTRUCTION_SMART_POLICY
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QuartzJobStatus
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QuartzJobStatus.NORMAL
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * # CreateOrModifyQuartzJobConfigurationPayload
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:37 PM
 */
class CreateOrModifyQuartzJobConfigurationPayload {
    lateinit var name: @NotBlank @Length(max = 50) String
    lateinit var group: @NotBlank @Length(max = 200) String
    lateinit var invokeTarget: @NotBlank @Length(max = 500) String
    lateinit var cronExpression: @NotBlank @Length(max = 50) String
    var misfirePolicy: @NotNull MisfirePolicy = MISFIRE_INSTRUCTION_SMART_POLICY
    var concurrent: @NotNull Concurrent = DISALLOW_CONCURRENT
    lateinit var description: @NotBlank @Length(max = 1000) String
    var status: @NotNull QuartzJobStatus = NORMAL

    fun asQuartzJobConfiguration(): QuartzJobConfiguration {
        val result = QuartzJobConfiguration()
        result.name = name
        result.group = group
        result.invokeTarget = invokeTarget
        result.cronExpression = cronExpression
        result.misfirePolicy = misfirePolicy.value
        result.concurrent = concurrent.value
        result.description = description
        result.status = status.value
        return result
    }
}
