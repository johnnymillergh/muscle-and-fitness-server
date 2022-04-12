package com.jmsoftware.maf.authcenter.user.payload

import com.jmsoftware.maf.common.bean.PaginationBase
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeGroup
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeType
import java.time.LocalDateTime

/**
 * # GetUserPageListPayload
 *
 * Description: GetUserPageList, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:46 PM
 */
class GetUserPageListPayload : PaginationBase() {
    var username: String? = null

    @DateTimeRangeGroup(type = DateTimeRangeType.START_TIME)
    val startTime: LocalDateTime? = null

    @DateTimeRangeGroup(type = DateTimeRangeType.END_TIME)
    val endTime: LocalDateTime? = null
}
