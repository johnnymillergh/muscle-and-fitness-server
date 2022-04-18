package com.jmsoftware.maf.authcenter.user.persistence

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import com.jmsoftware.maf.springcloudstarter.database.BasePersistenceEntity
import java.time.LocalDate

/**
 * # User
 *
 * User Persistence object class
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 7:31 AM
 */
@TableName(value = User.TABLE_NAME)
class User : BasePersistenceEntity() {
    companion object {
        const val TABLE_NAME = "user"
        const val COL_USERNAME = "username"
        const val COL_EMAIL = "email"
        const val COL_CELLPHONE = "cellphone"
        const val COL_PASSWORD = "password"
        const val COL_FULL_NAME = "full_name"
        const val COL_BIRTHDAY = "birthday"
        const val COL_GENDER = "gender"
        const val COL_AVATAR = "avatar"
        const val COL_STATUS = "status"
    }

    /**
     * Username
     */
    @TableField(value = COL_USERNAME)
    var username: String? = null

    /**
     * Email
     */
    @TableField(value = COL_EMAIL)
    var email: String? = null

    /**
     * Cellphone number
     */
    @TableField(value = COL_CELLPHONE)
    var cellphone: String? = null

    /**
     * Password
     */
    @TableField(value = COL_PASSWORD)
    var password: String? = null

    /**
     * Full name
     */
    @TableField(value = COL_FULL_NAME)
    var fullName: String? = null

    /**
     * Birthday
     */
    @TableField(value = COL_BIRTHDAY)
    var birthday: LocalDate? = null

    /**
     * 26 gender options
     */
    @TableField(value = COL_GENDER)
    var gender: String? = null

    /**
     * User avatar full path on SFTP server
     */
    @TableField(value = COL_AVATAR)
    var avatar: String? = null

    /**
     * Status. 1 - enabled, 2 - disabled
     */
    @TableField(value = COL_STATUS)
    var status: Byte = 0
}
