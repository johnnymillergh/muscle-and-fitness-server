package com.jmsoftware.maf.springcloudstarter.quartz.constant;

import com.jmsoftware.maf.common.bean.EnumerationBase;
import lombok.Getter;
import lombok.ToString;

/**
 * Description: QuartzJobStatus, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 10:11 AM
 **/
@Getter
@ToString
public enum QuartzJobStatus implements EnumerationBase<Byte> {
    /**
     * Normal
     */
    NORMAL((byte) 0),
    /**
     * Paused
     */
    PAUSE((byte) 1),
    ;

    private final Byte value;

    QuartzJobStatus(Byte value) {
        this.value = value;
    }
}
