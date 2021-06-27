package com.jmsoftware.maf.springcloudstarter.database;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: DatabaseContextHolder, thread-safe
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 12:07 AM
 **/
@Slf4j
public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceTypeEnum> CONTEXT_HOLDER = new ThreadLocal<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(-1);
    private static final int MAX_COUNT = 9999;

    public static DataSourceTypeEnum get() {
        return Optional.ofNullable(CONTEXT_HOLDER.get()).orElse(DataSourceTypeEnum.MASTER);
    }

    public static void set(DataSourceTypeEnum dbType) {
        CONTEXT_HOLDER.set(dbType);
    }

    static void master() {
        set(DataSourceTypeEnum.MASTER);
        if (log.isDebugEnabled()) {
            log.debug("Current data source -> {}", DataSourceTypeEnum.MASTER);
        }
    }

    static void slave() {
        // Simple load-balance for more slave clusters
        val index = COUNTER.getAndIncrement() % 2;
        if (COUNTER.get() > MAX_COUNT) {
            COUNTER.set(-1);
        }
        // if (index == 0) {
        //    set(DataSourceTypeEnum.SLAVE1);
        // }else {
        //     set(DataSourceTypeEnum.SLAVE2);
        // }
        set(DataSourceTypeEnum.SLAVE1);
        if (log.isDebugEnabled()) {
            log.debug("Current data source -> {}, index: {}", DataSourceTypeEnum.MASTER, index);
        }
    }

    static void clear() {
        CONTEXT_HOLDER.remove();
        if (log.isDebugEnabled()) {
            log.debug("Cleared CONTEXT_HOLDER");
        }
    }
}
