package com.jmsoftware.maf.common.domain.authcenter.permission

import com.jmsoftware.maf.common.util.logger

/**
 * # PermissionType
 *
 * Change description here
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 6:44 PM
 */
enum class PermissionType(val type: Byte, val description: String) {
    /**
     * Controller
     */
    CONTROLLER(0.toByte(), "Controller"),

    /**
     * Page
     */
    PAGE(1.toByte(), "Page"),

    /**
     * Button
     */
    BUTTON(2.toByte(), "Button (API)");

    companion object {
        private val log = logger()
        fun getByType(type: Byte): PermissionType? {
            var permissionType: PermissionType? = null
            val values = values()
            for (pt in values) {
                if (pt.type == type) {
                    permissionType = pt
                }
            }
            return permissionType
        }

        /**
         * Get enum by name
         *
         * @param name enum name
         * @return enum
         */
        fun getByName(name: String): PermissionType? {
            return try {
                valueOf(name)
            } catch (e: IllegalArgumentException) {
                log.error("Invalid enum name: $name", e)
                null
            }
        }
    }
}
