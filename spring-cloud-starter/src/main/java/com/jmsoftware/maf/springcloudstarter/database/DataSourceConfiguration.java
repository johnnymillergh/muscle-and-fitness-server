package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceCreatorAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Description: DataSourceConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 8:15 AM
 **/
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass({MybatisPlusAutoConfiguration.class, DynamicDataSourceCreatorAutoConfiguration.class})
public class DataSourceConfiguration {
    private final DynamicRoutingDataSource dynamicRoutingDataSource;

    /**
     * Primary data source. Had to configure DynamicRoutingDataSource as primary. Otherwise
     * MasterSlaveAutoRoutingPlugin will not be able to be injected datasource correctly.
     *
     * @return the data source
     * @see MasterSlaveAutoRoutingPlugin
     */
    @Bean
    @Primary
    public DataSource primaryDataSource() {
        log.info("Setting 'DynamicRoutingDataSource' as 'primaryDataSource', {}", dynamicRoutingDataSource);
        return dynamicRoutingDataSource;
    }

    @Bean
    @QuartzDataSource
    @ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
    public DataSource quartzDataSource() {
        val quartzDataSource = dynamicRoutingDataSource.getDataSource(DataSourceEnum.QUARTZ.getDataSourceName());
        log.info("Setting up quartzDataSource from 'DynamicRoutingDataSource', quartzDataSource: {}",
                 quartzDataSource.hashCode());
        return quartzDataSource;
    }

    @Bean
    @Primary
    public PlatformTransactionManager primaryPlatformTransactionManager(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
        log.info("Setting up the central interface in Spring's imperative transaction infrastructure " +
                         "'PlatformTransactionManager'. primaryDataSource: {}", primaryDataSource);
        return new DataSourceTransactionManager(primaryDataSource);
    }

    @Bean
    @QuartzTransactionManager
    @ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
    public PlatformTransactionManager quartzPlatformTransactionManager(@Qualifier("quartzDataSource") DataSource quartzDataSource) {
        log.info("Setting up the central interface in Spring's imperative transaction infrastructure " +
                         "'PlatformTransactionManager'. quartzDataSource: {}", quartzDataSource.hashCode());
        return new DataSourceTransactionManager(quartzDataSource);
    }
}
