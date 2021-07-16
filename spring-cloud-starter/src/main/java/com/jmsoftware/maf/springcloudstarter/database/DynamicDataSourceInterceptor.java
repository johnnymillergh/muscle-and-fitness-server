package com.jmsoftware.maf.springcloudstarter.database;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;

/**
 * Description: DynamicDataSourceInterceptor, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 12:26 AM
 **/
@Slf4j
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
                })
})
public class DynamicDataSourceInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // Check if the transaction synchronization is active. It it was, would use MASTER data source
        val actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        // MyBatis CRUD args
        val args = invocation.getArgs();
        // MappedStatement SqlCommandType
        val mappedStatement = (MappedStatement) args[0];
        // Default using master
        DataSourceContextHolder.master();
        if (!actualTransactionActive) {
            // SqlCommandType.SELECT, UNKNOWN|INSERT|UPDATE|DELETE|SELECT|FLUSH
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                // If it's the SQL return primary key as result
                if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    log.warn("Calling ID-return SQL, {}", SelectKeyGenerator.SELECT_KEY_SUFFIX);
                    DataSourceContextHolder.master();
                } else {
                    if (Objects.deepEquals(SqlCommandType.SELECT, mappedStatement.getSqlCommandType())) {
                        DataSourceContextHolder.slave();
                    } else {
                        DataSourceContextHolder.master();
                    }
                }
            }
        } else {
            DataSourceContextHolder.master();
        }
        log.warn("SQL statement [{}], SqlCommandType： [{}], using 🐬 [{}] data source", mappedStatement.getId(),
                 mappedStatement.getSqlCommandType().name(), DataSourceContextHolder.get());
        Object result;
        try {
            result = invocation.proceed();
        } finally {
            DataSourceContextHolder.clear();
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }
}

