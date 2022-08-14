package com.jmsoftware.maf.springcloudstarter.redis

import cn.hutool.core.util.RandomUtil
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.util.logger
import org.springframework.integration.redis.util.RedisLockRegistry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * # RedisDistributedLockDemoController
 *
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/26/22 2:31 PM
 */
@RestController
@RequestMapping("/redis/distributed-lock")
class RedisDistributedLockDemoController(
    private val redisLockRegistry: RedisLockRegistry
) {
    companion object {
        private val log = logger()
    }

    @GetMapping("/resource/{resourceKey}/{autoUnlock}")
    fun accessRaceResource(
        @PathVariable resourceKey: String,
        @PathVariable autoUnlock: Boolean
    ): ResponseBodyBean<Map<String, Any>> {
        return ResponseBodyBean.ofSuccess(lockRaceResource(resourceKey, autoUnlock))
    }

    private fun lockRaceResource(key: String, autoUnlock: Boolean): Map<String, Any> {
        val lock = redisLockRegistry.obtain(key)
        log.info("Obtained the lock from the registry: $lock, Key: $key")
        val acquiredLock = lock.tryLock()
        if (acquiredLock && autoUnlock) {
            log.warn("Acquired the lock. Mocking to do busy computes. Will release the lock automatically")
            Thread.sleep(RandomUtil.randomInt(10000).toLong())
            lock.unlock()
            log.warn("Released the lock. $lock")
        }
        log.info("Lock: $lock, Key: $key, acquiredLock: $acquiredLock")
        return mapOf("acquiredLock" to acquiredLock, "lock" to lock, "lockString" to lock.toString())
    }
}
