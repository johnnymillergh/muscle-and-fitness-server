package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourcePropertiesCustomizer;
import com.baomidou.dynamic.datasource.support.DdConstants;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description: MyBatisPlusConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/29/2021 1:30 PM
 **/
@Slf4j
@Configuration
@EnableTransactionManagement
@Import({
        DataSourceConfiguration.class
})
@MapperScan("com.jmsoftware.maf.springcloudstarter.*.mapper")
@ConditionalOnClass({MybatisPlusAutoConfiguration.class, MasterSlaveAutoRoutingPlugin.class})
public class MyBatisPlusConfiguration {
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        log.warn("Initial bean: '{}'", PaginationInnerInterceptor.class.getSimpleName());
        val paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(100L);
        return paginationInnerInterceptor;
    }

    @Bean
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        log.warn("Initial bean: '{}'", BlockAttackInnerInterceptor.class.getSimpleName());
        return new BlockAttackInnerInterceptor();
    }

    /**
     * Mybatis plus interceptor mybatis plus interceptor.
     *
     * @param paginationInnerInterceptor  the pagination inner interceptor
     * @param blockAttackInnerInterceptor the block attack inner interceptor
     * @return the mybatis plus interceptor
     * @see <a href='https://baomidou.com/guide/interceptor.html'>MybatisPlusInterceptor</a>
     */
    @Bean
    @Order(1)
    public Interceptor mybatisPlusInterceptor(PaginationInnerInterceptor paginationInnerInterceptor,
                                              BlockAttackInnerInterceptor blockAttackInnerInterceptor) {
        log.warn("Initial bean: '{}'", MybatisPlusInterceptor.class.getSimpleName());
        val mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        mybatisPlusInterceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    /**
     * Register master-slave auto routing plugin interceptor. Mybatis-Plus doesn't support non-master-slave
     * datasource yet.
     *
     * @return the interceptor
     * @see DdConstants
     */
    @Bean
    @Order(2)
    public Interceptor masterSlaveAutoRoutingPlugin() {
        log.warn("Initial bean: '{}'", MasterSlaveAutoRoutingPlugin.class.getSimpleName());
        return new MasterSlaveAutoRoutingPlugin();
    }

    @Bean
    public CommonMetaObjectHandler commonMetaObjectHandler() {
        log.warn("Initial bean: '{}'", CommonMetaObjectHandler.class.getSimpleName());
        return new CommonMetaObjectHandler();
    }

    @Bean
    public DynamicDataSourcePropertiesCustomizer dynamicDataSourcePropertiesCustomizer() {
        return properties -> {
            val cpuCoreCount = Runtime.getRuntime().availableProcessors();
            val minConnectionPoolSize = cpuCoreCount * 2 + 1;
            val maxConnectionPoolSize = cpuCoreCount * 3;
            val druidConfig = properties.getDruid();
            druidConfig.setInitialSize(minConnectionPoolSize);
            druidConfig.setMinIdle(minConnectionPoolSize);
            druidConfig.setMaxActive(maxConnectionPoolSize);
            log.warn("Druid connection pool enhanced by current cpuCoreCount: {}, initial size: {}, min idle: {}" +
                             ", max active: {}",
                     cpuCoreCount, minConnectionPoolSize, minConnectionPoolSize, maxConnectionPoolSize);
        };
    }
}
