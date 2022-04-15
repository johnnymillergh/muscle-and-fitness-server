package com.jmsoftware.maf.springcloudstarter.redis

import com.jmsoftware.maf.common.util.logger
import lombok.extern.slf4j.Slf4j
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.integration.redis.util.RedisLockRegistry

/**
 * # RedisDistributedLockConfiguration
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 9:41 PM
 */
@Slf4j
@ConditionalOnClass(RedisLockRegistry::class)
class RedisDistributedLockConfiguration {
    companion object {
        private const val REGISTRY_KEY = "redis-lock"
        private val log = logger()
    }

    /**
     * Redis distributed lock registry.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the redis lock registry
     * @see <a href='https://docs.spring.io/spring-integration/docs/current/reference/html/redis.html.redis-lock-registry'>Redis Lock Registry</a>
     */
    @Bean(destroyMethod = "destroy")
    @ConditionalOnClass(RedisLockRegistry::class)
    fun redisLockRegistry(redisConnectionFactory: RedisConnectionFactory): RedisLockRegistry {
        return RedisLockRegistry(redisConnectionFactory, REGISTRY_KEY).apply {
            log.warn("RedisLockRegistry bean is created. `${RedisLockRegistry::class.java.simpleName}`")
        }
    }

    @Bean
    @ConditionalOnClass(RedisLockRegistry::class)
    fun redisDistributedLockDemoController(
        redisLockRegistry: RedisLockRegistry
    ): RedisDistributedLockDemoController {
        return RedisDistributedLockDemoController(redisLockRegistry).apply {
            log.warn("RedisDistributedLockDemoController is created")
        }
    }
}
