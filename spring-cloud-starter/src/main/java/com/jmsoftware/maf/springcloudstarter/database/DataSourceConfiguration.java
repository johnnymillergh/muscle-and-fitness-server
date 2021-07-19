package com.jmsoftware.maf.springcloudstarter.database;

import cn.hutool.db.ds.DataSourceWrapper;
import com.alibaba.druid.util.JdbcUtils;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class})
public class DataSourceConfiguration {
    @Bean
    @QuartzDataSource
    @ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
    public DataSource quartzDataSource(DynamicRoutingDataSource dynamicRoutingDataSource) {
        log.info("Getting quartzDataSource from 'DynamicRoutingDataSource'");
        return dynamicRoutingDataSource.getDataSource(DataSourceEnum.QUARTZ.getDataSourceName());
    }

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
        log.info("Wrapping 'DynamicRoutingDataSource' as 'primaryDataSource', jdbcUrl: {}, driverClassName: {}",
                 jdbcUrl, driverClassName);
        return DataSourceWrapper.wrap(dynamicRoutingDataSource, driverClassName);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(@Qualifier("primaryDataSource") DataSource primaryDataSource) {
        return new DataSourceTransactionManager(primaryDataSource);
    }
}
