package com.jmsoftware.authcenter.universal.configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <h1>MyBatisPlusConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-05-02 11:57
 **/
@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfiguration {
    /**
     * Inject bean to enable pagination.
     *
     * @return bean for pagination
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        var paginationInterceptor = new PaginationInterceptor();
        // Set maximum query record count
        paginationInterceptor.setLimit(100L);
        // Enable JSQL Parser Count Optimizing (for left join)
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
}
