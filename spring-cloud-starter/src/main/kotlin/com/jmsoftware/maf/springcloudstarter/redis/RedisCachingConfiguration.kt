package com.jmsoftware.maf.springcloudstarter.redis

import com.jmsoftware.maf.common.util.logger
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.*
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.lang.reflect.Method

/**
 * # RedisCachingConfiguration
 *
 * Description: RedisCachingConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 9:08 PM
 * @see <a href='https://blog.csdn.net/echizao1839/article/details/102660649'>Spring boot 之 spring-boot-starter-cache （整合 Redis）</a>
 */
@EnableCaching
@ConditionalOnClass(RedisConnectionFactory::class)
class RedisCachingConfiguration(
    private val redisConnectionFactory: RedisConnectionFactory
) : CachingConfigurerSupport() {
    companion object {
        private val log = logger()
    }

    override fun cacheManager(): CacheManager {
        return RedisCacheManager
            .builder(redisConnectionFactory)
            .cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                    .disableCachingNullValues()
                    .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                            GenericJackson2JsonRedisSerializer()
                        )
                    )
            )
            .build().apply {
                log.warn("Initial bean: `${CacheManager::class.java.simpleName}`")
            }
    }

    override fun cacheResolver(): CacheResolver {
        return SimpleCacheResolver(cacheManager()).apply {
            log.warn("Initial bean: `${SimpleCacheResolver::class.java.simpleName}`")
        }
    }

    override fun keyGenerator(): KeyGenerator {
        return KeyGenerator { target: Any, method: Method, params: Array<Any> ->
            val stringBuilder = StringBuilder()
            stringBuilder.append(target.javaClass.name).append("#").append(method.name)
            for (param in params) {
                stringBuilder.append(":").append(param.toString())
            }
            stringBuilder.toString()
        }.apply {
            log.warn("Initial bean: `${KeyGenerator::class.java.simpleName}`")
        }
    }

    override fun errorHandler(): CacheErrorHandler {
        return SimpleCacheErrorHandler().apply {
            log.warn("Initial bean: `${CacheErrorHandler::class.java.simpleName}`")
        }
    }
}
