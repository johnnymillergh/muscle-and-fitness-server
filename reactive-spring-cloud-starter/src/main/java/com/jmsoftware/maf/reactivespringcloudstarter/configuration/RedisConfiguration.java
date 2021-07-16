package com.jmsoftware.maf.reactivespringcloudstarter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;

/**
 * Description: RedisConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/30/2020 1:08 PM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {
    @PostConstruct
    private void postConstruct() {
        log.warn("Initial bean: '{}'", RedisConfiguration.class.getSimpleName());
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
