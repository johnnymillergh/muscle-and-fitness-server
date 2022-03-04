package com.jmsoftware.maf.common.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: DeletedField, change description here.
 *
 * @author 钟俊 （zhongjun）, email: zhongjun@toguide.cn, date: 1/13/2021 6:32 PM
 */
@Slf4j
@Getter
@ToString
public enum DeletedField {
    /**
     * Not deleted
     */
    NOT_DELETED("N", "not deleted"),
    /**
     * Deleted
     */
    DELETED("Y", "deleted");

    /**
     * The Value.
     */
    private final String value;
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
    DeletedField(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
