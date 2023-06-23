package com.jmsoftware.maf.springcloudstarter.redis

import com.jmsoftware.maf.common.util.Slf4j
import org.apache.commons.collections4.MapUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.integration.redis.util.RedisLockRegistry
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.locks.Lock

/**
 * # RedisDistributedLockDemoControllerTest
 *
 * Description: RedisDistributedLockDemoControllerTest, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 6/23/2023 6:34 PM
 */
@Slf4j
@ExtendWith(MockitoExtension::class)
class RedisDistributedLockDemoControllerTest {
    @InjectMocks
    lateinit var redisDistributedLockDemoController: RedisDistributedLockDemoController

    @Mock
    lateinit var redisLockRegistry: RedisLockRegistry

    @Mock
    lateinit var lock: Lock

    @Test
    fun accessRaceResource_whenObtainTheLockWithAutoUnlock_thenAcquiredLockIsTrue() {
        whenever(redisLockRegistry.obtain(anyString())).thenReturn(lock)
        whenever(lock.tryLock(anyLong(), any(TimeUnit::class.java))).thenReturn(true)
        assertDoesNotThrow {
            val response =
                redisDistributedLockDemoController.accessRaceResource("fakeResourceKey", 10, SECONDS, true)
            assertTrue(MapUtils.isNotEmpty(response.data))
            assertTrue(response.data?.containsKey("acquiredLock") ?: false)
            assertTrue(response.data?.get("acquiredLock") as Boolean)
            assertTrue(response.data?.containsKey("lock") ?: false)
            assertTrue(response.data?.containsKey("lockString") ?: false)
        }
    }

    @Test
    fun accessRaceResource_whenNotObtainTheLockWithAutoUnlock_thenAcquiredLockIsFalse() {
        whenever(redisLockRegistry.obtain(anyString())).thenReturn(lock)
        whenever(lock.tryLock(anyLong(), any(TimeUnit::class.java))).thenReturn(false)
        assertDoesNotThrow {
            val response =
                redisDistributedLockDemoController.accessRaceResource("fakeResourceKey", 10, SECONDS, true)
            assertTrue(MapUtils.isNotEmpty(response.data))
            assertTrue(response.data?.containsKey("acquiredLock") ?: false)
            assertFalse(response.data?.get("acquiredLock") as Boolean)
            assertTrue(response.data?.containsKey("lock") ?: false)
            assertTrue(response.data?.containsKey("lockString") ?: false)
        }
    }
}
