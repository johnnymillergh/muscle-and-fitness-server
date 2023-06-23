package com.jmsoftware.maf.springcloudstarter.quartz.entity

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

/**
 * # QuartzJobConfigurationExcel
 *
 * Description: QuartzJobConfigurationExcel, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:39 PM
 */
class QuartzJobConfigurationExcel {
    var name: @NotBlank String? = null
    var group: String? = null
    var serviceName: String? = null
    var invokeTarget: @NotBlank String? = null
    var cronExpression: @NotBlank String? = null
    var misfirePolicy: Byte? = null
    var concurrent: Byte? = null
    var description: @NotBlank String? = null
    var status: Byte? = null
    var createdBy: String? = null
    var createdTime: LocalDateTime? = null
    var modifiedBy: String? = null
    var modifiedTime: LocalDateTime? = null
}
