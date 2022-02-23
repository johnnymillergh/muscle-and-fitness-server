package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * <h1>DistributedLockConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/23/22 7:51 AM
 **/
@Slf4j
@ConditionalOnClass({RedisLockRegistry.class})
public class DistributedLockConfiguration {
    private static final String REGISTRY_KEY = "redis-lock";

    @Bean(destroyMethod = "destroy")
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        val redisLockRegistry = new RedisLockRegistry(redisConnectionFactory, REGISTRY_KEY);
        log.warn("RedisLockRegistry bean is created. {}", redisLockRegistry);
        return redisLockRegistry;
    }
}
