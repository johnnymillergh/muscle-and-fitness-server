package com.jmsoftware.maf.authcenter.user.payload

import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus
import javax.validation.constraints.NotNull

/**
 * # GetUserStatusPayload
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:50 PM
 */
class GetUserStatusPayload {
    @NotNull
    lateinit var status: UserStatus
}
