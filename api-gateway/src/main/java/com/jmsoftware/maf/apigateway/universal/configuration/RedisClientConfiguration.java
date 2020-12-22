package com.jmsoftware.maf.apigateway.universal.configuration;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * <h1>RedisClientConfiguration</h1>
 * <p>
 * Ignored request configuration.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Slf4j
@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisClientConfiguration extends CachingConfigurerSupport {
    /**
     * Redis template. Support for &lt;String, Serializable&gt;
     */
    @Bean
    public RedisTemplate<String, Serializable> redisFactory(LettuceConnectionFactory lettuceConnectionFactory) {
        log.info("Initial bean: {}", RedisTemplate.class.getSimpleName());
        val template = new RedisTemplate<String, Serializable>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }

    /**
     * Reactive redis template factory.
     *
     * @param factory the reactive redis connection factory
     * @return the reactive redis template
     */
    @Bean
    ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        log.info("Initial bean: {}", ReactiveRedisTemplate.class.getSimpleName());
        Jackson2JsonRedisSerializer<Serializable> serializer = new Jackson2JsonRedisSerializer<>(Serializable.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Serializable> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, Serializable> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
