package com.jmsoftware.maf.authcenter.role.constant

/**
 * # RoleRedisKey
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:37 PM
 */
enum class RoleRedisKey(
    val keyInfixFormat: String
) {
    /**
     * Get user by login token key pattern, expired in random [1, 7) days
     */
    GET_ROLE_LIST_BY_USER_ID(":role:get_role_list_by_user_id:");
}
