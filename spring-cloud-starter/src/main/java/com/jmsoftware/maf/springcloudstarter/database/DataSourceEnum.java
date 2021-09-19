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
     * Main
     */
    MAIN_1("source_1"),
    /**
     * Replica 1
     */
    REPLICA_1("replica_1"),
    /**
     * Quartz
     */
    QUARTZ("quartz"),
    ;

    private final String dataSourceName;
}
