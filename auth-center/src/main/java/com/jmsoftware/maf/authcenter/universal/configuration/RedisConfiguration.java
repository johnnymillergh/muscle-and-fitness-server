package com.jmsoftware.maf.authcenter.universal.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description: RedisConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/30/2020 1:08 PM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    private final ObjectMapper objectMapper;

    /**
     * RedisTemplate uses JDK byte code serialization (byte[]), which is not that readable and friendly to
     * human reading.
     * <p>
     * In order to replace that, we have to <b>customize</b> RedisTemplate.
     *
     * @param redisConnectionFactory the redis connection factory
     * @return RedisTemplate redis template
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/30/2020 1:18 PM
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.warn("Initial bean: {}", RedisTemplate.class.getSimpleName());

        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

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
     */
    @Bean
    ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        log.warn("Initial bean: {}", ReactiveRedisTemplate.class.getSimpleName());
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        RedisSerializationContext<Object, Object> serializationContext = RedisSerializationContext
                .newSerializationContext(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .value(valueSerializer)
                .hashValue(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
}
