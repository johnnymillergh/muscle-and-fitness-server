package com.jmsoftware.maf.authcenter.role.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h1>RoleRedisKey</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 6/28/21 9:45 PM
 **/
@Getter
@RequiredArgsConstructor
public enum RoleRedisKey {
    /**
     * Get user by login token key pattern, expired in random [1, 7) days
     */
    GET_ROLE_LIST_BY_USER_ID(":role:get_role_list_by_user_id:");

    private final String keyInfixFormat;
}
