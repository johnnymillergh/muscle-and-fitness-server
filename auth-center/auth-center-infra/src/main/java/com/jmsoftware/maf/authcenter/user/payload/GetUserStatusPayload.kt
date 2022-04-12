package com.jmsoftware.maf.authcenter.user.payload

import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus
import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus2
import com.jmsoftware.maf.springcloudstarter.validation.annotation.ValidEnumValue

/**
 * # GetUserStatusPayload
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:50 PM
 */
class GetUserStatusPayload {
    @ValidEnumValue(targetEnum = UserStatus::class)
    var status: Byte? = null

    @ValidEnumValue(targetEnum = UserStatus2::class)
    var status2: Byte? = null
}
