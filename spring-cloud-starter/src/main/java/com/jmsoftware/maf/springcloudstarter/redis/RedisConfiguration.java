package com.jmsoftware.maf.springcloudstarter.redis;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * Description: RedisConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/30/2020 1:08 PM
 **/
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({
        RedisMasterSlaveReplicationProperties.class
})
@Import({
        RedisCachingConfiguration.class
})
@ConditionalOnClass({RedisConnectionFactory.class})
public class RedisConfiguration {
    private static final String REGISTRY_KEY = "redis-lock";
    private final RedisMasterSlaveReplicationProperties redisMasterSlaveReplicationProperties;
    private final ObjectMapper objectMapper;

    /**
     * Redis connection factory lettuce connection factory.
     *
     * @return the lettuce connection factory
     * @see
     * <a href='https://docs.spring.io/spring-data/redis/docs/current/reference/html/#redis:write-to-master-read-from-replica'>Spring Data Redis - Write to Master, Read from Replica</a>
     */
    @Bean
    @ConditionalOnProperty(prefix = "redis.master", name = "host")
    public LettuceConnectionFactory redisConnectionFactory() {
        if (CollUtil.isEmpty(this.redisMasterSlaveReplicationProperties.getSlaves())) {
            throw new IllegalArgumentException(
                    "Redis Master/Replica configuration is not right! To fix this issue, specify slaves configuration");
        }
        val redisStaticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(
                this.redisMasterSlaveReplicationProperties.getMaster().getHost(),
                this.redisMasterSlaveReplicationProperties.getMaster().getPort());
        redisStaticMasterReplicaConfiguration.setPassword(
                this.redisMasterSlaveReplicationProperties.getMaster().getPassword());
        this.redisMasterSlaveReplicationProperties.getSlaves()
                .forEach(slave -> redisStaticMasterReplicaConfiguration.addNode(slave.getHost(), slave.getPort()));
        val lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        log.warn("Initial bean: '{}' for Redis Master/Replica configuration",
                 LettuceConnectionFactory.class.getSimpleName());
        return new LettuceConnectionFactory(redisStaticMasterReplicaConfiguration, lettuceClientConfiguration);
    }

    /**
     * RedisTemplate uses JDK byte code serialization (byte[]), which is not that readable and friendly to
     * human reading.
     * <p>
     * In order to replace that, we have to <b>customize</b> RedisTemplate.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return RedisTemplate redis template
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/30/2020 1:18 PM
     * @see
     * <a href='https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:template'>Working with Objects through RedisTemplate</a>
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.warn("Initial bean: '{}'", RedisTemplate.class.getSimpleName());

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(this.objectMapper);

        // Set key serializers as StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Use Jackson2JsonRedisSerialize to replace default JDK serialization
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * Reactive redis template factory.
     *
     * @param connectionFactory the reactive redis connection factory
     * @return the reactive redis template
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/30/2020 1:43 PM
     * @see
     * <a href='https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:reactive:template'>Working with Objects through ReactiveRedisTemplate</a>
     */
    @Bean
    ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        log.warn("Initial bean: '{}'", ReactiveRedisTemplate.class.getSimpleName());
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        RedisSerializationContext<Object, Object> serializationContext = RedisSerializationContext
                .newSerializationContext(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .value(valueSerializer)
                .hashValue(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

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
