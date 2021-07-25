package com.jmsoftware.maf.springcloudstarter.database;

import cn.hutool.db.ds.DataSourceWrapper;
import com.alibaba.druid.util.JdbcUtils;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.SneakyThrows;
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
@ConditionalOnClass({MybatisPlusAutoConfiguration.class})
public class DataSourceConfiguration {
    /**
     * Primary data source. Had to configure DynamicRoutingDataSource as primary. Otherwise
     * MasterSlaveAutoRoutingPlugin will not be able to be injected datasource correctly.
     *
     * @param dynamicRoutingDataSource the dynamic routing data source
     * @return the data source
     * @see MasterSlaveAutoRoutingPlugin
     */
    @Bean
    @Primary
    @SneakyThrows
    public DataSource primaryDataSource(DynamicRoutingDataSource dynamicRoutingDataSource,
                                        DynamicDataSourceProperties dynamicDataSourceProperties) {
        val jdbcUrl = dynamicDataSourceProperties
                .getDatasource()
                .get(dynamicDataSourceProperties.getPrimary())
                .getUrl();
        val driverClassName = JdbcUtils.getDriverClassName(jdbcUrl);
        val wrappedDataSource = DataSourceWrapper.wrap(dynamicRoutingDataSource, driverClassName);
        log.info("Wrapping 'DynamicRoutingDataSource' as 'primaryDataSource', jdbcUrl: {}, driverClassName: {}, " +
                         "wrappedDataSource: {}", jdbcUrl, driverClassName, wrappedDataSource);
        return wrappedDataSource;
    }

    @Bean
    @QuartzDataSource
    @ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
    public DataSource quartzDataSource(DynamicRoutingDataSource dynamicRoutingDataSource) {
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
