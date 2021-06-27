package com.jmsoftware.maf.springcloudstarter.database;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Description: DynamicDataSource, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 12:06 AM
 **/
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.get();
    }
}
