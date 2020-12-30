package com.jmsoftware.maf.exercisemis.universal.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <h1>MyBatisPlusConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-05-02 11:57
 **/
@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfiguration {
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        val paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(100L);
        return paginationInnerInterceptor;
    }

    @Bean
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        // 新的分页插件，一缓和二缓遵循 MyBatis 的规则。
        // 需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题（该属性会在旧插件移除后一同移除）
        return configuration -> configuration.setUseDeprecatedExecutor(false);
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
    public MybatisPlusInterceptor mybatisPlusInterceptor(PaginationInnerInterceptor paginationInnerInterceptor,
                                                         BlockAttackInnerInterceptor blockAttackInnerInterceptor) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        mybatisPlusInterceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return mybatisPlusInterceptor;
    }
}
