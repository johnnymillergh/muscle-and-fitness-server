package com.jmsoftware.maf.reactivespringcloudstarter.redis;

import cn.hutool.core.collection.CollUtil;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Description: RedisConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/31/2021 1:08 PM
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
    private final RedisMasterSlaveReplicationProperties redisMasterSlaveReplicationProperties;


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
     * Reactive redis template factory.
     *
     * @param connectionFactory the reactive redis connection factory
     * @return the reactive redis template
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/30/2020 1:43 PM
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
}
