package com.jmsoftware.maf.authcenter.user.constant

/**
 * # UserRedisKey
 *
 * Description: UserRedisKey, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:43 PM
 */
enum class UserRedisKey(
    val keyInfixFormat: String
) {
    /**
     * Get user by login token key pattern, expired in random [1, 7) days
     */
    GET_USER_BY_LOGIN_TOKEN(":user:get_user_by_login_token:%s");
}
