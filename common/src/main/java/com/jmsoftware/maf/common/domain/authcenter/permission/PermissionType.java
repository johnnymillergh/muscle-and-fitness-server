package com.jmsoftware.maf.common.domain.authcenter.permission;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <h1>PermissionType</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-05-25 10:24
 **/
@Slf4j
@Getter
public enum PermissionType {
    /**
     * Controller
     */
    CONTROLLER(Byte.valueOf("0"), "Controller"),
    /**
     * Page
     */
    PAGE(Byte.valueOf("1"), "Page"),
    /**
     * Button
     */
    BUTTON(Byte.valueOf("2"), "Button (API)");

    private final Byte type;
    private final String description;

    PermissionType(Byte type, String description) {
        this.type = type;
        this.description = description;
    }

    public static PermissionType getByType(Byte type) {
        PermissionType permissionType = null;
        PermissionType[] values = PermissionType.values();
        for (PermissionType pt : values) {
            if (pt.getType().equals(type)) {
                permissionType = pt;
            }
        }
        return permissionType;
    }

    /**
     * Get enum by name
     *
     * @param name enum name
     * @return enum
     */
    public static PermissionType getByName(String name) {
        try {
            return PermissionType.valueOf(name);
        } catch (IllegalArgumentException e) {
            log.error("Invalid enum name: {}", name, e);
            return null;
        }
    }
}
