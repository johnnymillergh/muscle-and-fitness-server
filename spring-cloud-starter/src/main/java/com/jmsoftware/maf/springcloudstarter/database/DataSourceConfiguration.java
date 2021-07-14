package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * Description: DataSourceConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 8:15 AM
 **/
@Slf4j
@ConditionalOnClass({MybatisPlusAutoConfiguration.class})
@AutoConfigureBefore({MybatisPlusAutoConfiguration.class})
public class DataSourceConfiguration {
    @QuartzDataSource
    @Bean("quartzDataSource")
    @ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
    public DataSource quartzDataSource(DynamicRoutingDataSource dynamicRoutingDataSource) {
        log.warn("Initial bean: quartzDataSource");
        return dynamicRoutingDataSource.getDataSource(DataSourceEnum.QUARTZ.getDataSourceName());
    }

    @Bean
    @Primary
    public ReadWriteIsolationDynamicRoutingDataSource dynamicDataSource(DynamicRoutingDataSource dynamicRoutingDataSource) {
        val targetDataSources = new HashMap<>(4);
        targetDataSources.put(DataSourceEnum.MASTER,
                              dynamicRoutingDataSource.getDataSource(DataSourceEnum.MASTER.getDataSourceName()));
        targetDataSources.put(DataSourceEnum.SLAVE1,
                              dynamicRoutingDataSource.getDataSource(DataSourceEnum.SLAVE1.getDataSourceName()));
        val dynamicDataSource = new ReadWriteIsolationDynamicRoutingDataSource();
        dynamicDataSource.setDefaultTargetDataSource(dynamicRoutingDataSource.getDataSource("master"));
        dynamicDataSource.setTargetDataSources(targetDataSources);
        log.warn("Set 'masterDataSource' and 'slave1DataSource' as {}",
                 ReadWriteIsolationDynamicRoutingDataSource.class.getSimpleName());
        return dynamicDataSource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(ReadWriteIsolationDynamicRoutingDataSource readWriteIsolationDynamicRoutingDataSource) {
        return new DataSourceTransactionManager(readWriteIsolationDynamicRoutingDataSource);
    }
}
