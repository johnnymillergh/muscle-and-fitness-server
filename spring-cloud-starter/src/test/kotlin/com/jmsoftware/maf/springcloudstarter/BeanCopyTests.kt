package com.jmsoftware.maf.springcloudstarter

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.date.StopWatch
import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.springframework.beans.BeanUtils
import java.time.LocalDateTime

/**
 * # BeanCopyTests
 *
 * Description: BeanCopyTests, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:22 AM
 */
@Execution(ExecutionMode.CONCURRENT)
internal class BeanCopyTests {
    companion object {
        private const val COUNT = 1_000_000
        var log = logger()
    }

    @Test
    fun hutoolBeanCopyTests() {
        val beanOne = buildBeanOne()
        val stopwatch = StopWatch()
        stopwatch.start()
        for (i in 0 until COUNT) {
            val beanTwo = BeanTwo()
            BeanUtil.copyProperties(beanOne, beanTwo)
        }
        stopwatch.stop()
        log.info("Hutool BeanUtil took ${stopwatch.totalTimeSeconds}s (${stopwatch.totalTimeMillis}ms)")
        Assertions.assertNotNull(beanOne)
    }

    @Test
    fun springBeanCopyTests() {
        val beanOne = buildBeanOne()
        val stopwatch = StopWatch()
        stopwatch.start()
        for (i in 0 until COUNT) {
            val beanTwo = BeanTwo()
            BeanUtils.copyProperties(beanOne, beanTwo)
        }
        stopwatch.stop()
        log.info("Spring BeanUtil took ${stopwatch.totalTimeSeconds}s (${stopwatch.totalTimeMillis}ms)")
        Assertions.assertNotNull(beanOne)
    }

    private fun buildBeanOne(): BeanOne {
        val beanOne = BeanOne()
        beanOne.id = 865L
        beanOne.name = "Eas"
        beanOne.description = "S6XC"
        beanOne.createdBy = 485L
        beanOne.createdTime = LocalDateTime.now()
        beanOne.modifiedBy = 865L
        beanOne.modifiedTime = LocalDateTime.now()
        return beanOne
    }

    private class BeanOne {
        var id: Long = 0L
        lateinit var  name: String
        lateinit var  description: String
        var  createdBy: Long = 0L
        lateinit var  createdTime: LocalDateTime
        var  modifiedBy: Long = 0L
        lateinit var  modifiedTime: LocalDateTime
    }

    @Suppress("unused")
    private class BeanTwo {
        var id: Long = 0L
        lateinit var  name: String
        lateinit var  description: String
        lateinit var  createdTime: LocalDateTime
        lateinit var  modifiedTime: LocalDateTime
    }
}
