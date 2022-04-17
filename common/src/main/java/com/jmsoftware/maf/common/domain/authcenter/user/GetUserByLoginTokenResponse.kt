package com.jmsoftware.maf.common.domain.authcenter.user

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * # GetUserByLoginTokenResponse
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 7:37 PM
 */
class GetUserByLoginTokenResponse {
    /**
     * Primary key
     */
    var id: Long? = null

    /**
     * Username
     */
    var username: String? = null

    /**
     * Email
     */
    var email: String? = null

    /**
     * Cellphone number
     */
    var cellphone: String? = null

    /**
     * Password
     */
    var password: String? = null

    /**
     * Nickname
     */
    var fullName: String? = null

    /**
     * Birthday (yyyy-MM-dd)
     */
    var birthday: LocalDate? = null

    /**
     * 58 gender options
     */
    var gender: String? = null

    /**
     * User avatar full path on SFTP server
     */
    var avatar: String? = null

    /**
     * Status. 1 - enabled, 2 - disabled
     */
    var status: Byte? = null

    /**
     * Create time
     */
    var createdTime: LocalDateTime? = null

    /**
     * Modify time
     */
    var modifiedTime: LocalDateTime? = null
}
