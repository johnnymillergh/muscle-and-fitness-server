package com.jmsoftware.maf.common.domain.authcenter.user

/**
 * # UserStatus
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date 2019-03-23 18:48
 */
enum class UserStatus(
    val value: Byte,
    val description: String
) {
    /**
     * Enabled user
     */
    ENABLED(1.toByte(), "Enabled user"),

    /**
     * Disabled user
     */
    DISABLED(0.toByte(), "Disabled user");
}
