package com.jmsoftware.maf.common.constant;

import lombok.Getter;

/**
 * Description: MafHttpHeader, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/28/2021 1:20 PM
 **/
@Getter
public enum MafHttpHeader {
    /**
     * X-Id
     */
    X_ID("X-Id"),
    /**
     * X-Username
     */
    X_USERNAME("X-Username"),
    ;

    private final String header;

    MafHttpHeader(String header) {
        this.header = header;
    }
}
