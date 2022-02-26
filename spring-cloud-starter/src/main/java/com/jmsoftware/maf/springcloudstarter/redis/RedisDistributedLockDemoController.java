package com.jmsoftware.maf.springcloudstarter.redis;

import cn.hutool.core.util.RandomUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <h1>RedisDistributedLockDemoController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/26/22 2:31 PM
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/distributed-lock")
public class RedisDistributedLockDemoController {
    private final RedisLockRegistry redisLockRegistry;

    @GetMapping("/resource/{resourceKey}/{autoUnlock}")
    public ResponseBodyBean<String> accessRaceResource(
            @PathVariable String resourceKey,
            @PathVariable boolean autoUnlock
    ) {
        return ResponseBodyBean.ofSuccess(this.lockRaceResource(resourceKey, autoUnlock).toString());
    }

    @SneakyThrows
    private Lock lockRaceResource(String key, boolean autoUnlock) {
        val lock = this.redisLockRegistry.obtain(key);
        log.info("Obtained the lock from the registry: {}, Key: {}", lock, key);
        val acquiredLock = lock.tryLock(3, TimeUnit.SECONDS);
        if (acquiredLock && autoUnlock) {
            log.warn("Acquired the lock. Mocking to do busy computes. Will release the lock automatically");
            Thread.sleep(RandomUtil.randomInt(10000));
            lock.unlock();
            log.warn("Released the lock. {}", lock);
        }
        log.info("Lock: {}, Key: {}, acquiredLock: {}", lock, key, acquiredLock);
        return lock;
    }
}
