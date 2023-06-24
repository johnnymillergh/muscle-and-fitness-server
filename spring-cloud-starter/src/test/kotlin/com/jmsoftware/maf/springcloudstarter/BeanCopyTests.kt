package com.jmsoftware.maf.springcloudstarter

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.date.StopWatch
import com.jmsoftware.maf.common.util.logger
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.BeanUtils
import java.time.LocalDateTime

/**
 * # BeanCopyTests
 *
 * Description: BeanCopyTests, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:22 AM
 */
class BeanCopyTests {
    companion object {
        private const val COUNT = 100
        var log = logger()
    }

    @Test
    fun testHutoolBeanCopy() {
        val stopWatch = StopWatch("Hutool Bean Copy Test")
        val beanOne = buildBeanOne()
        for (i in 1..COUNT) {
            val beanTwo = BeanTwo()
            stopWatch.start("Loop #$i")
            BeanUtil.copyProperties(beanOne, beanTwo)
            stopWatch.stop()
            assertNotNull(beanTwo)
        }
        assertNotNull(beanOne)
        log.info(stopWatch.prettyPrint())
    }

    @Test
    fun testSpringBeanCopy() {
        val beanOne = buildBeanOne()
        val stopWatch = StopWatch("Spring Bean Copy Test")
        for (i in 1..COUNT) {
            stopWatch.start("Loop #$i")
            val beanTwo = BeanTwo()
            BeanUtils.copyProperties(beanOne, beanTwo)
            stopWatch.stop()
            assertNotNull(beanTwo)
        }
        assertNotNull(beanOne)
        log.info(stopWatch.prettyPrint())
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
        lateinit var name: String
        lateinit var description: String
        var createdBy: Long = 0L
        lateinit var createdTime: LocalDateTime
        var modifiedBy: Long = 0L
        lateinit var modifiedTime: LocalDateTime
    }

    @Suppress("unused")
    private class BeanTwo {
        var id: Long = 0L
        lateinit var name: String
        lateinit var description: String
        lateinit var createdTime: LocalDateTime
        lateinit var modifiedTime: LocalDateTime
    }
}
