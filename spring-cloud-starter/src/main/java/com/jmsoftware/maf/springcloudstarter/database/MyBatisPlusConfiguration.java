package com.jmsoftware.maf.springcloudstarter.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description: MyBatisPlusConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 1/29/2021 1:30 PM
 **/
@Slf4j
@Configuration
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
    public ConfigurationCustomizer configurationCustomizer() {
        log.warn("Initial bean: '{}'", ConfigurationCustomizer.class.getSimpleName());
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
        log.warn("Initial bean: '{}'", MybatisPlusInterceptor.class.getSimpleName());
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        mybatisPlusInterceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    @Bean
    public MyBatisPlusMetaObjectHandler myBatisPlusConfiguration() {
        log.warn("Initial bean: '{}'", MyBatisPlusMetaObjectHandler.class.getSimpleName());
        return new MyBatisPlusMetaObjectHandler();
    }
}
