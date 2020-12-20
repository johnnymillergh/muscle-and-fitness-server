package com.jmsoftware.maf.gateway.universal.configuration;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <h1>RedisServiceImpl</h1>
 * <p>Redis service implementation for Redis useful operations</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-07-05 09:39
 **/
@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Serializable> redisTemplate;
    private final ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate;

    public RedisServiceImpl(@Qualifier("redisFactory") RedisTemplate<String, Serializable> redisTemplate,
                            @Qualifier("reactiveRedisTemplate") ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Boolean set(String key, String value, Long expirationTime, TimeUnit timeUnit) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            Boolean result = connection.set(Objects.requireNonNull(serializer.serialize(key)),
                    Objects.requireNonNull(serializer.serialize(value)),
                    Expiration.from(expirationTime, timeUnit),
                    RedisStringCommands.SetOption.upsert());
            return ObjectUtil.isNotNull(result) ? result : false;
        });
    }

    @Override
    public Boolean set(String key, List<Serializable> list, Long expirationTime, TimeUnit timeUnit) {
        String value = JSONUtil.toJsonStr(list);
        return set(key, value, expirationTime, timeUnit);
    }

    @Override
    public Boolean set(String key, String value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            Boolean result = connection.set(Objects.requireNonNull(serializer.serialize(key)),
                    Objects.requireNonNull(serializer.serialize(value)));
            return ObjectUtil.isNotNull(result) ? result : false;
        });
    }

    @Override
    public Boolean set(String key, List<Serializable> list) {
        String value = JSONUtil.toJsonStr(list);
        return set(key, value);
    }

    @Override
    public Boolean expire(String key, long expirationTime, TimeUnit timeUnit) {
        Boolean result = redisTemplate.expire(key, expirationTime, timeUnit);
        return ObjectUtil.isNotNull(result) ? result : false;
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public String get(String key) {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] bytes = connection.get(Objects.requireNonNull(serializer.serialize(key)));
            return serializer.deserialize(bytes);
        });
    }

    @Override
    public List<Serializable> get(String key, Class<Serializable> clazz) {
        String json = get(key);
        if (StrUtil.isNotBlank(json)) {
            return JSONUtil.toList(JSONUtil.parseArray(json), clazz);
        }
        return null;
    }

    @Override
    public Long delete(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            return connection.del(serializer.serialize(key));
        });
    }

    @Override
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }
}
