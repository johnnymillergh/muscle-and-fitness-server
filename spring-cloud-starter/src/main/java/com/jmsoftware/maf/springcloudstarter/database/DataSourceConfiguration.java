package com.jmsoftware.maf.springcloudstarter.database;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
    @Bean
    @ConfigurationProperties("spring.datasource.dynamic.datasource.master")
    public DataSource masterDataSource() {
        log.warn("Initial bean: masterDataSource");
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.dynamic.datasource.slave1")
    public DataSource slave1DataSource() {
        log.warn("Initial bean: slave1DataSource");
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicRoutingDataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                                      @Qualifier("slave1DataSource") DataSource slave1DataSource) {
        log.warn("Loading masterDataSource and slave1DataSource as DynamicDataSource");
        val targetDataSources = new HashMap<>(4);
        targetDataSources.put(DataSourceTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DataSourceTypeEnum.SLAVE1, slave1DataSource);
        val dynamicDataSource = new DynamicRoutingDataSource();
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DynamicRoutingDataSource dynamicRoutingDataSource) {
        return new DataSourceTransactionManager(dynamicRoutingDataSource);
    }
}
