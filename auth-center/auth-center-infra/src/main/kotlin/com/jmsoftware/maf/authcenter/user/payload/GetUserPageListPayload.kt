package com.jmsoftware.maf.authcenter.user.payload

import com.jmsoftware.maf.common.bean.PaginationBase
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * # GetUserPageListPayload
 *
 * Description: GetUserPageList, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:46 PM
 */
data class GetUserPageListPayload(
    @field:NotEmpty
    val username: String,
    @field:NotNull
    val startTime: LocalDateTime,
    @field:NotNull
    val endTime: LocalDateTime
) : PaginationBase()
