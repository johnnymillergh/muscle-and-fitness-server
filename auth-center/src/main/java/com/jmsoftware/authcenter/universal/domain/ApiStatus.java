package com.jmsoftware.authcenter.universal.domain;

import lombok.Getter;

/**
 * Description: ApiStatus, change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-04-07 23:32
 **/
@Getter
public enum ApiStatus {
    /**
     * Idled API (not stored in db).
     */
    IDLED(0),
    /**
     * API in used (stored in db).
     */
    IN_USE(1);

    private final Integer status;

    ApiStatus(Integer status) {
        this.status = status;
    }

    /**
     * Get ApiStatus by status.
     *
     * @param status status code
     * @return ApiStatus enum
     */
    public static ApiStatus getByStatus(Integer status) {
        ApiStatus[] apiStatuses = ApiStatus.values();
        for (ApiStatus apiStatus : apiStatuses) {
            if (apiStatus.status.equals(status)) {
                return apiStatus;
            }
        }
        return null;
    }
}
