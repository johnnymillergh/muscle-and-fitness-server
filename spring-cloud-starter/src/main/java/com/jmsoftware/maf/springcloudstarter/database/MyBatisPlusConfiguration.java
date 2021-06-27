package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description: MyBatisPlusConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/29/2021 1:30 PM
 **/
@Slf4j
@Configuration
@Import({
        DataSourceConfiguration.class
})
@ConditionalOnClass({MybatisPlusAutoConfiguration.class})
@EnableTransactionManagement
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

    @Bean
    public DynamicDataSourceInterceptor dynamicDataSourceInterceptor() {
        log.warn("Initial bean: '{}'", DynamicDataSourceInterceptor.class.getSimpleName());
        return new DynamicDataSourceInterceptor();
    }

    @Bean
    public MyBatisPlusMetaObjectHandler myBatisPlusConfiguration() {
        log.warn("Initial bean: '{}'", MyBatisPlusMetaObjectHandler.class.getSimpleName());
        return new MyBatisPlusMetaObjectHandler();
    }
}
