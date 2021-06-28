package com.jmsoftware.maf.authcenter.user.entity.constant;

import lombok.Getter;

/**
 * Description: UserRedisKey, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/28/2021 3:18 PM
 **/
@Getter
public enum UserRedisKey {
    /**
     * Get user by login token key pattern, expired in random [1, 7) days
     */
    GET_USER_BY_LOGIN_TOKEN(":user:get_user_by_login_token:%s");
    private final String keyInfixFormat;

    UserRedisKey(String keyInfixFormat) {
        this.keyInfixFormat = keyInfixFormat;
    }
}
