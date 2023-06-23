package com.jmsoftware.maf.springcloudstarter.redis

import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.util.Slf4j
import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import org.apache.commons.lang3.RandomUtils
import org.apache.commons.lang3.ThreadUtils
import org.hibernate.validator.constraints.Range
import org.springframework.integration.redis.util.RedisLockRegistry
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

/**
 * # RedisDistributedLockDemoController
 *
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/26/22 2:31 PM
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/redis/distributed-lock")
class RedisDistributedLockDemoController(
    private val redisLockRegistry: RedisLockRegistry
) {
    @GetMapping("/race-resources/{resourceKey}/{timeout}/{timeoutUnit}/{autoUnlock}")
    fun accessRaceResource(
        @PathVariable resourceKey: String,
        @PathVariable @Range(min = 0) timeout: Long,
        @PathVariable timeoutUnit: TimeUnit,
        @PathVariable autoUnlock: Boolean
    ): ResponseBodyBean<Map<String, Any>> {
        return ResponseBodyBean.ofSuccess(lockRaceResource(resourceKey, timeout, timeoutUnit, autoUnlock))
    }

    private fun lockRaceResource(
        key: String,
        timeout: Long,
        timeoutUnit: TimeUnit,
        autoUnlock: Boolean
    ): Map<String, Any> {
        log.warn("Obtaining lock for key: $key with timeout: $timeout, unit: $timeoutUnit, autoUnlock: $autoUnlock")
        val lock = redisLockRegistry.obtain(key)
        log.info("Obtained the lock from the registry: $lock, key: $key")
        val acquiredLock = lock.tryLock(timeout, timeoutUnit)
        if (acquiredLock && autoUnlock) {
            val waitingTimeInSeconds = RandomUtils.nextInt(1, 10).toLong()
            log.warn("Acquired the lock. Mocking to do busy computes for $waitingTimeInSeconds seconds. Will release the lock automatically")
            ThreadUtils.sleep(Duration.of(waitingTimeInSeconds, ChronoUnit.SECONDS))
            lock.unlock()
            log.warn("Released the lock. $lock, key: $key")
        }
        log.info("Lock: $lock, Key: $key, acquiredLock: $acquiredLock")
        return mapOf("acquiredLock" to acquiredLock, "lock" to lock, "lockString" to lock.toString())
    }
}
