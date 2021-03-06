package com.jmsoftware.maf.common.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: DeletedField, change description here.
 *
 * @author 钟俊 （zhongjun）, email: zhongjun@toguide.cn, date: 1/13/2021 6:32 PM
 */
@Slf4j
@Getter
public enum DeletedField {
    /**
     * Not deleted
     */
    NOT_DELETED((byte) 0, "not deleted"),
    /**
     * Deleted
     */
    DELETED((byte) 1, "deleted");

    /**
     * The Value.
     */
    private final Byte value;
    /**
     * The Description.
     */
    private final String description;

    /**
     * Instantiates a new Delete flag.
     *
     * @param value       the value
     * @param description the description
     */
    DeletedField(Byte value, String description) {
        this.value = value;
        this.description = description;
    }
}
