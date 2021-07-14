package com.jmsoftware.maf.springcloudstarter.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Description: DataSourceEnum, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 12:03 AM
 **/
@Getter
@ToString
@RequiredArgsConstructor
public enum DataSourceEnum {
    /**
     * Master
     */
    MASTER("master"),
    /**
     * Slave 1
     */
    SLAVE1("slave1"),
    /**
     * Quartz
     */
    QUARTZ("quartz"),
    ;

    private final String dataSourceName;
}
