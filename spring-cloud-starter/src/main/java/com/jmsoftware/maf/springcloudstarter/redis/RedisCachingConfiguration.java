package com.jmsoftware.maf.springcloudstarter.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.Objects;

import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

/**
 * Description: RedisCachingConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/30/2020 11:48 AM
 * @see
 * <a href='https://blog.csdn.net/echizao1839/article/details/102660649'>Spring boot 之 spring-boot-starter-cache （整合 Redis）</a>
 **/
@Slf4j
@Configuration
@EnableCaching
@RequiredArgsConstructor
@ConditionalOnClass({RedisConnectionFactory.class})
public class RedisCachingConfiguration extends CachingConfigurerSupport {
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Override
    public CacheManager cacheManager() {
        log.warn("Initial bean: '{}'", CacheManager.class.getSimpleName());
        RedisCacheConfiguration cacheConfiguration = defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager
                .builder(this.redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        log.warn("Initial bean: '{}'", SimpleCacheResolver.class.getSimpleName());
        return new SimpleCacheResolver(Objects.requireNonNull(this.cacheManager()));
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        log.warn("Initial bean: '{}'", KeyGenerator.class.getSimpleName());
        return (target, method, params) -> {
            val stringBuilder = new StringBuilder();
            stringBuilder.append(target.getClass().getName()).append("#").append(method.getName());
            for (Object param : params) {
                stringBuilder.append(":").append(param.toString());
            }
            return stringBuilder.toString();
        };
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        log.warn("Initial bean: '{}'", CacheErrorHandler.class.getSimpleName());
        return new SimpleCacheErrorHandler();
    }
}
