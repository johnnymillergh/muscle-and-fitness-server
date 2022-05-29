package com.jmsoftware.maf.springcloudstarter.quartz.entity

import java.time.LocalDateTime

/**
 * # GetQuartzJobConfigurationPageListItem
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:38 PM
 */
class GetQuartzJobConfigurationPageListItem {
    var id: Long? = null
    var name: String? = null
    var group: String? = null
    var serviceName: String? = null
    var invokeTarget: String? = null
    var cronExpression: String? = null
    var misfirePolicy: Byte? = null
    var concurrent: Byte? = null
    var description: String? = null
    var status: Byte? = null
    var createdBy: String? = null
    var createdTime: LocalDateTime? = null
    var modifiedBy: String? = null
    var modifiedTime: LocalDateTime? = null
}
