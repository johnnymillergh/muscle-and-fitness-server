package com.jmsoftware.maf.springcloudstarter.quartz.constant;

import com.jmsoftware.maf.common.bean.EnumerationBase;
import lombok.Getter;

/**
 * Description: Concurrent, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 2:04 PM
 **/
@Getter
public enum Concurrent implements EnumerationBase<Byte> {
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
}
