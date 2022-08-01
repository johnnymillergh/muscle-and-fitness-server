package com.jmsoftware.maf.authcenter.user.payload

import com.jmsoftware.maf.common.bean.PaginationBase

/**
 * # GetUserPageListPayload
 *
 * Description: GetUserPageList, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:46 PM
 */
class GetUserPageListPayload : PaginationBase() {
    val username: String? = null
}
