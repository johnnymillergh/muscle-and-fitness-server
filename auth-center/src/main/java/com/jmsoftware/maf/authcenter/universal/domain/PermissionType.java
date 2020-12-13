package com.jmsoftware.maf.authcenter.universal.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <h1>PermissionType</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-05-25 10:24
 **/
@Slf4j
@Getter
public enum PermissionType {
    /**
     * Controller
     */
    CONTROLLER(0, "Controller"),
    /**
     * Page
     */
    PAGE(1, "Page"),
    /**
     * Button
     */
    BUTTON(2, "Button (API)");

    private final Integer type;
    private final String description;

    PermissionType(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public static PermissionType getByType(Integer type) {
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
