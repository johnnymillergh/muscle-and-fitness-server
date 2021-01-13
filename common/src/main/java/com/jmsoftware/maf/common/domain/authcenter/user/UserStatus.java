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

    private final Byte status;
    private final String description;

    UserStatus(Byte status, String description) {
        this.status = status;
        this.description = description;
    }

    /**
     * Get user status enum by status value
     *
     * @param status status value
     * @return user status enum
     */
    public static UserStatus getByStatus(Byte status) {
        UserStatus result = UserStatus.DISABLED;
        UserStatus[] userStatuses = UserStatus.values();
        for (UserStatus userStatus : userStatuses) {
            if (userStatus.status.equals(status)) {
                result = userStatus;
            }
        }
        return result;
    }
}
