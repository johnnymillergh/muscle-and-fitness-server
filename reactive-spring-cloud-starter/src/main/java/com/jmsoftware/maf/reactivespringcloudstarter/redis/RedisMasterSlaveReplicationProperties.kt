package com.jmsoftware.maf.reactivespringcloudstarter.redis

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Description: RedisMasterSlaveReplicationProperties, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/31/2021 10:37 AM
 * @see <a href='https://redis.io/topics/replication'>Redis master-slave replication</a>
 * @see <a href='https://www.vinsguru.com/redis-master-slave-with-spring-boot/'>Redis Master Slave With Spring Boot</a>
 * @see <a href='https://docs.spring.io/spring-data/redis/docs/current/reference/html/#redis:write-to-master-read-from-replica'>Spring Data Redis - Write to Master, Read from Replica</a>
 */
@Configuration
@ConfigurationProperties(prefix = "redis")
class RedisMasterSlaveReplicationProperties {
    var master: RedisInstance? = null
    var slaves: List<RedisInstance>? = null

    class RedisInstance {
        var host: String? = null
        var port = 0
        var password: String? = null
    }
}
