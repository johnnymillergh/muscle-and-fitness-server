package com.jmsoftware.apiportal.universal.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h1>RedisService</h1>
 * <p>Redis service interface for Redis useful operations</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-07-05 09:38
 **/
public interface RedisService {
    /**
     * Set key and value in Redis with expiration
     *
     * @param key            key
     * @param value          value
     * @param expirationTime expiration time
     * @param timeUnit       time unit
     * @return true - operation done; false - operation failure
     */
    Boolean set(String key, String value, Long expirationTime, TimeUnit timeUnit);

    /**
     * Set key and list in Redis with expiration
     *
     * @param key            key
     * @param list           list
     * @param expirationTime expiration time
     * @param timeUnit       time unit
     * @return true - operation done; false - operation failure
     */
    Boolean set(String key, List<Serializable> list, Long expirationTime, TimeUnit timeUnit);

    /**
     * Set key and value in Redis
     *
     * @param key   key
     * @param value value
     * @return true - operation done; false - operation failure
     */
    Boolean set(String key, String value);

    /**
     * Set key and list in Redis
     *
     * @param key  key
     * @param list list
     * @return true - operation done; false - operation failure
     */
    Boolean set(String key, List<Serializable> list);

    /**
     * Make key expire
     *
     * @param key            key
     * @param expirationTime expiration time
     * @param timeUnit       time unit
     * @return true - operation done; false - operation failure
     */
    Boolean expire(String key, long expirationTime, TimeUnit timeUnit);

    /**
     * Get expiration time by key
     *
     * @param key      key
     * @param timeUnit time unit
     * @return expiration time. Null when used in pipeline / transaction.
     */
    Long getExpire(String key, TimeUnit timeUnit);

    /**
     * Get value by key
     *
     * @param key key
     * @return null when key does not exist or used in pipeline / transaction.
     */
    String get(String key);

    /**
     * Get list by key
     *
     * @param key   key
     * @param clazz type
     * @return null when key does not exist or used in pipeline / transaction.
     */
    List<Serializable> get(String key, Class<Serializable> clazz);

    /**
     * Delete by key
     *
     * @param key key
     * @return the number of keys that were removed. Null when used in pipeline / transaction.
     */
    Long delete(String key);

    /**
     * Delete by keys
     *
     * @param keys key list
     * @return the number of keys that were removed. Null when used in pipeline / transaction.
     */
    Long delete(Collection<String> keys);
}
