package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.plugin.Interceptor;
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
        log.warn("Initial bean array: '{}'", MybatisPlusInterceptor.class.getSimpleName());
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        mybatisPlusInterceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    @Bean
    @Order(2)
    public Interceptor dynamicDataSourceInterceptor() {
        log.warn("Initial bean: '{}'", DynamicDataSourceInterceptor.class.getSimpleName());
        return new DynamicDataSourceInterceptor();
    }

    @Bean
    public MyBatisPlusMetaObjectHandler myBatisPlusConfiguration() {
        log.warn("Initial bean: '{}'", MyBatisPlusMetaObjectHandler.class.getSimpleName());
        return new MyBatisPlusMetaObjectHandler();
    }
}
