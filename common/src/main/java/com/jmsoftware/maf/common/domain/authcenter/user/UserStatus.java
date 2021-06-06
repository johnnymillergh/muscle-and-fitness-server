package com.jmsoftware.maf.common.domain.authcenter.user;

import lombok.Getter;

/**
 * Description: UserStatus, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 18:48
 **/
@Getter
public enum UserStatus {
    /**
     * Enabled user
     */
    ENABLED((byte) 1, "Enabled user"),
    /**
     * Disabled user
     */
    DISABLED((byte) 0, "Disabled user");

    private final Byte value;
    private final String description;

    UserStatus(Byte value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * Get user value enum by value value
     *
     * @param value value value
     * @return user value enum
     */
    public static UserStatus ofValue(Byte value) {
        UserStatus result = UserStatus.DISABLED;
        UserStatus[] userStatuses = UserStatus.values();
        for (UserStatus userStatus : userStatuses) {
            if (userStatus.value.equals(value)) {
                result = userStatus;
            }
        }
        return result;
    }
}
