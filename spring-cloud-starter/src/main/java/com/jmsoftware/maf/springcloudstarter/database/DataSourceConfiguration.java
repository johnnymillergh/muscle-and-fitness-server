package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.quartz.core.QuartzScheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.QuartzTransactionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
@ConditionalOnClass({MybatisPlusAutoConfiguration.class, QuartzScheduler.class})
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties("spring.shardingsphere.datasource.quartz")
    @ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
    public DataSourceProperties quartzDataSourceProperties() {
        val quartzDataSourceProperties = new DataSourceProperties();
        log.info("Setting up quartzDataSourceProperties: {}", quartzDataSourceProperties.getName());
        return quartzDataSourceProperties;
    }

    @Bean
    @QuartzDataSource
    @ConditionalOnProperty(prefix = "spring.quartz", name = "job-store-type", havingValue = "jdbc")
    public DataSource quartzDataSource(DataSourceProperties quartzDataSourceProperties) {
        val dataSource = quartzDataSourceProperties.initializeDataSourceBuilder().build();
        log.info("Setting up quartzDataSource: {}", dataSource);
        return dataSource;
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
