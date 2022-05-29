package com.jmsoftware.maf.springcloudstarter.quartz.entity

import com.jmsoftware.maf.common.bean.PaginationBase

/**
 * # GetQuartzJobConfigurationPageListPayload
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:38 PM
 */
class GetQuartzJobConfigurationPageListPayload : PaginationBase() {
    var serviceName: String? = null
}
