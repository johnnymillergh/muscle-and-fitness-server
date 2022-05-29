package com.jmsoftware.maf.common.domain.authcenter.user;

import com.jmsoftware.maf.common.enumeration.ValueDescriptionBaseEnum;
import lombok.Getter;
import lombok.ToString;

/**
 * Description: UserStatus, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 18:48
 **/
@Getter
@ToString
public enum UserStatus2 implements ValueDescriptionBaseEnum<Byte> {
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

    UserStatus2(Byte value, String description) {
        this.value = value;
        this.description = description;
    }
}
