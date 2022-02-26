package com.jmsoftware.maf.springcloudstarter.redis;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * <h1>RedisDistributedLockConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/26/22 11:19 PM
 **/
@Slf4j
@ConditionalOnClass({RedisLockRegistry.class})
public class RedisDistributedLockConfiguration {
    private static final String REGISTRY_KEY = "redis-lock";

    /**
     * Redis distributed lock registry.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the redis lock registry
     * @see
     * <a href='https://docs.spring.io/spring-integration/docs/current/reference/html/redis.html#redis-lock-registry'>Redis Lock Registry</a>
     */
    @Bean(destroyMethod = "destroy")
    @ConditionalOnClass({RedisLockRegistry.class})
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        val redisLockRegistry = new RedisLockRegistry(redisConnectionFactory, REGISTRY_KEY);
        log.warn("RedisLockRegistry bean is created. {}", redisLockRegistry);
        return redisLockRegistry;
    }

    @Bean
    @ConditionalOnClass({RedisLockRegistry.class})
    public RedisDistributedLockDemoController redisDistributedLockDemoController(
            RedisLockRegistry redisLockRegistry
    ) {
        log.warn("RedisDistributedLockDemoController is created");
        return new RedisDistributedLockDemoController(redisLockRegistry);
    }
}
