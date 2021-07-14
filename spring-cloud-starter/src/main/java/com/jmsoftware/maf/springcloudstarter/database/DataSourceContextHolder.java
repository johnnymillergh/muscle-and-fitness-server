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
public final class DataSourceContextHolder {
    protected static final ThreadLocal<DataSourceEnum> CONTEXT_HOLDER =
            ThreadLocal.withInitial(() -> DataSourceEnum.MASTER);
    private static final AtomicInteger COUNTER = new AtomicInteger(-1);
    private static final int MAX_COUNT = 9999;

    private DataSourceContextHolder() {
    }

    public static DataSourceEnum get() {
        return Optional.ofNullable(CONTEXT_HOLDER.get()).orElse(DataSourceEnum.MASTER);
    }

    private static void set(DataSourceEnum dbType) {
        CONTEXT_HOLDER.set(dbType);
    }

    public static void master() {
        set(DataSourceEnum.MASTER);
        if (log.isDebugEnabled()) {
            log.debug("Current data source -> {}", DataSourceEnum.MASTER);
        }
    }

    public static void slave() {
        // Simple load-balance for more slave clusters, assumed we got 2 replicas
        val index = COUNTER.getAndIncrement() % 2;
        if (COUNTER.get() > MAX_COUNT) {
            COUNTER.set(-1);
        }
        // if replica data sources are more then 1, the data source needs load balance by index
        set(DataSourceEnum.SLAVE1);
        if (log.isDebugEnabled()) {
            log.debug("Current data source -> {}, index: {}", DataSourceEnum.MASTER, index);
        }
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
        if (log.isDebugEnabled()) {
            log.debug("Cleared CONTEXT_HOLDER");
        }
    }
}
