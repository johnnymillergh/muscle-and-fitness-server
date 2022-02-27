package com.jmsoftware.maf.springcloudstarter.quartz.constant;

import com.jmsoftware.maf.common.enumeration.ValueDescriptionBaseEnum;
import lombok.Getter;
import lombok.ToString;

/**
 * Description: Concurrent, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 2:04 PM
 **/
@Getter
@ToString
public enum Concurrent implements ValueDescriptionBaseEnum<Byte> {
    /**
     * Disallow Concurrent
     */
    DISALLOW_CONCURRENT((byte) 0),
    /**
     * concurrent
     */
    CONCURRENT((byte) 1),
    ;
    private final Byte value;

    Concurrent(Byte value) {
        this.value = value;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
