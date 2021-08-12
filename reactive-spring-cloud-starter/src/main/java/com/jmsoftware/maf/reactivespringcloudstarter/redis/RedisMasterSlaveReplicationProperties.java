package com.jmsoftware.maf.reactivespringcloudstarter.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Description: RedisMasterSlaveReplicationProperties, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/31/2021 10:37 AM
 * @see <a href='https://redis.io/topics/replication'>Redis master-slave replication</a>
 * @see <a href='https://www.vinsguru.com/redis-master-slave-with-spring-boot/'>Redis Master Slave With Spring Boot</a>
 * @see
 * <a href='https://docs.spring.io/spring-data/redis/docs/current/reference/html/#redis:write-to-master-read-from-replica'>Spring Data Redis - Write to Master, Read from Replica</a>
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisMasterSlaveReplicationProperties {
    private RedisInstance master;
    private List<RedisInstance> slaves;

    @Data
    public static class RedisInstance {
        private String host;
        private int port;
        private String password;
    }
}
