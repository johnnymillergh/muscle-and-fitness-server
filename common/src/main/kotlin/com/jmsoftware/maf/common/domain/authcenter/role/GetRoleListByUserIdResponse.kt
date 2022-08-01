package com.jmsoftware.maf.common.domain.authcenter.role

import java.io.Serial
import java.io.Serializable

/**
 * # GetRoleListByUserIdResponse
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 6:52 PM
 */
class GetRoleListByUserIdResponse : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = -8462678958191383914L
    }

    lateinit var roleList: List<GetRoleListByUserIdSingleResponse>
}

/**
 * # GetRoleListByUserIdSingleResponse
 *
 * Description: GetRoleListByUserIdSingleResponse, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 6:56 PM
 */
class GetRoleListByUserIdSingleResponse : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 3758434255123050684L
    }

    var id: Long = 0L

    lateinit var name: String
}
