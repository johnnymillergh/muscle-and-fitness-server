package com.jmsoftware.maf.springcloudstarter.redis

import cn.hutool.core.collection.CollUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.function.requireTrue
import com.jmsoftware.maf.common.util.logger
import io.lettuce.core.ReadFrom
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.util.function.Consumer

/**
 * # RedisConfiguration
 *
 * Description: RedisConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 9:40 PM
 */
@EnableConfigurationProperties(RedisMasterSlaveReplicationProperties::class)
@Import(RedisCachingConfiguration::class, RedisDistributedLockConfiguration::class)
@ConditionalOnClass(RedisConnectionFactory::class)
class RedisConfiguration(
    private val redisMasterSlaveReplicationProperties: RedisMasterSlaveReplicationProperties,
    private val objectMapper: ObjectMapper
) {
    companion object {
        private val log = logger()
    }

    /**
     * Redis connection factory lettuce connection factory. Only takes effect when the 'redis.master.host' is
     * configured.
     *
     * @return the lettuce connection factory
     * @see <a href='https://docs.spring.io/spring-data/redis/docs/current/reference/html/.redis:write-to-master-read-from-replica'>Spring Data Redis - Write to Master, Read from Replica</a>
     */
    @Bean
    @ConditionalOnProperty(prefix = "redis.master", name = ["host"])
    fun redisConnectionFactory(): LettuceConnectionFactory {
        requireTrue(CollUtil.isNotEmpty(redisMasterSlaveReplicationProperties.slaves), null).orElseThrow {
            IllegalStateException("Redis Master/Replica configuration is not right! To fix this issue, specify slaves configuration")
        }
        val redisStaticMasterReplicaConfiguration = RedisStaticMasterReplicaConfiguration(
            redisMasterSlaveReplicationProperties.master?.host!!,
            redisMasterSlaveReplicationProperties.master?.port!!
        ).apply { setPassword(redisMasterSlaveReplicationProperties.master?.password) }
        redisMasterSlaveReplicationProperties.slaves
            ?.forEach(Consumer { slave: RedisMasterSlaveReplicationProperties.RedisInstance ->
                redisStaticMasterReplicaConfiguration.addNode(
                    slave.host!!,
                    slave.port
                )
            })
        return LettuceConnectionFactory(
            redisStaticMasterReplicaConfiguration, LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build()
        ).apply {
            log.warn("Initial bean: `${LettuceConnectionFactory::class.java.simpleName}` for Redis Master/Replica configuration")
        }
    }

    /**
     * RedisTemplate uses JDK byte code serialization (byte[]), which is not that readable and friendly to
     * human reading.
     *
     * In order to replace that, we have to **customize** RedisTemplate.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return RedisTemplate redis template
     * @see <a href='https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/.redis:template'>Working with Objects through RedisTemplate</a>
     */
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<Any, Any> {
        return RedisTemplate<Any, Any>().apply {
            connectionFactory = redisConnectionFactory
            keySerializer = RedisSerializer.string()
            valueSerializer = Jackson2JsonRedisSerializer(objectMapper, Any::class.java)
            hashKeySerializer = RedisSerializer.string()
            hashValueSerializer = Jackson2JsonRedisSerializer(objectMapper, Any::class.java)
            afterPropertiesSet()
            log.warn("Initial bean: `${RedisTemplate::class.java.simpleName}`")
        }
    }

    /**
     * Reactive redis template factory.
     *
     * @param connectionFactory the reactive redis connection factory
     * @return the reactive redis template
     * @see <a href='https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/.redis:reactive:template'>Working with Objects through ReactiveRedisTemplate</a>
     */
    @Bean
    fun reactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<Any, Any> {
        val valueSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        return ReactiveRedisTemplate(
            connectionFactory, RedisSerializationContext
                .newSerializationContext<Any, Any>(
                    RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string())
                )
                .value(valueSerializer)
                .hashValue(valueSerializer)
                .build()
        ).apply {
            log.warn("Initial bean: `${ReactiveRedisTemplate::class.java.simpleName}`")
        }
    }
}
