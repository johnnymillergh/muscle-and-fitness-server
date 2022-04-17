package com.jmsoftware.maf.springcloudstarter.database

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.database.CommonMetaObjectHandler
import org.apache.ibatis.plugin.Interceptor
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * # MyBatisPlusConfiguration
 *
 * Description: MyBatisPlusConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 11:37 AM
 */
@EnableTransactionManagement
@ConditionalOnClass(MybatisPlusAutoConfiguration::class)
@MapperScan("com.jmsoftware.maf.springcloudstarter.*.repository")
class MyBatisPlusConfiguration {
    companion object {
        private val log = logger()
    }

    @Bean
    fun paginationInnerInterceptor(): PaginationInnerInterceptor {
        log.warn("Initial bean: `${PaginationInnerInterceptor::class.java.simpleName}`")
        val paginationInnerInterceptor = PaginationInnerInterceptor(DbType.MYSQL)
        paginationInnerInterceptor.maxLimit = 100L
        return paginationInnerInterceptor
    }

    @Bean
    fun blockAttackInnerInterceptor(): BlockAttackInnerInterceptor {
        log.warn("Initial bean: `${BlockAttackInnerInterceptor::class.java.simpleName}`")
        return BlockAttackInnerInterceptor()
    }

    @Bean
    fun optimisticLockerInnerInterceptor(): OptimisticLockerInnerInterceptor {
        log.warn("Initial bean: `${OptimisticLockerInnerInterceptor::class.java.simpleName}`")
        return OptimisticLockerInnerInterceptor()
    }

    /**
     * Mybatis plus interceptor mybatis plus interceptor.
     *
     * @param paginationInnerInterceptor       the pagination inner interceptor
     * @param blockAttackInnerInterceptor      the block attack inner interceptor
     * @param optimisticLockerInnerInterceptor the optimistic locker inner interceptor
     * @return the mybatis plus interceptor
     * @see <a href='https://baomidou.com/guide/interceptor.html'>MybatisPlusInterceptor</a>
     */
    @Bean
    @Order(1)
    fun mybatisPlusInterceptor(
        paginationInnerInterceptor: PaginationInnerInterceptor,
        blockAttackInnerInterceptor: BlockAttackInnerInterceptor,
        optimisticLockerInnerInterceptor: OptimisticLockerInnerInterceptor
    ): Interceptor {
        log.warn("Initial bean: `${MybatisPlusInterceptor::class.java.simpleName}`")
        val mybatisPlusInterceptor = MybatisPlusInterceptor()
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor)
        mybatisPlusInterceptor.addInnerInterceptor(blockAttackInnerInterceptor)
        mybatisPlusInterceptor.addInnerInterceptor(optimisticLockerInnerInterceptor)
        return mybatisPlusInterceptor
    }

    @Bean
    fun commonMetaObjectHandler(): CommonMetaObjectHandler {
        log.warn("Initial bean: `${CommonMetaObjectHandler::class.java.simpleName}`")
        return CommonMetaObjectHandler()
    }
}
