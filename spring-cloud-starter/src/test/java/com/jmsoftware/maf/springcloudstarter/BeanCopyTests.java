package com.jmsoftware.maf.springcloudstarter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import lombok.Data;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * Description: BeanCopyTests, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 10/21/2021 8:07 PM
 **/
@Execution(ExecutionMode.CONCURRENT)
class BeanCopyTests {
    private static final int COUNT = 1_000_000;

    @Test
    void hutoolBeanCopyTests() {
        val beanOne = this.buildBeanOne();
        val stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < COUNT; i++) {
            val beanTwo = new BeanTwo();
            BeanUtil.copyProperties(beanOne, beanTwo);
        }
        stopwatch.stop();
        Console.log("Hutool BeanUtil took {}s ({}ms)", stopwatch.getTotalTimeSeconds(), stopwatch.getTotalTimeMillis());
        Assertions.assertNotNull(beanOne);
    }

    @Test
    void springBeanCopyTests() {
        val beanOne = this.buildBeanOne();
        val stopwatch = new StopWatch();
        stopwatch.start();
        for (int i = 0; i < COUNT; i++) {
            val beanTwo = new BeanTwo();
            BeanUtils.copyProperties(beanOne, beanTwo);
        }
        stopwatch.stop();
        Console.log("Spring BeanUtils took {}s ({}ms)", stopwatch.getTotalTimeSeconds(),
                    stopwatch.getTotalTimeMillis());
        Assertions.assertNotNull(beanOne);
    }

    private BeanOne buildBeanOne() {
        val beanOne = new BeanOne();
        beanOne.setId(865L);
        beanOne.setName("Eas");
        beanOne.setDescription("S6XC");
        beanOne.setCreatedBy(485L);
        beanOne.setCreatedTime(LocalDateTime.now());
        beanOne.setModifiedBy(849L);
        beanOne.setModifiedTime(LocalDateTime.now());
        return beanOne;
    }

    @Data
    private static class BeanOne {
        private Long id;
        private String name;
        private String description;
        private Long createdBy;
        private LocalDateTime createdTime;
        private Long modifiedBy;
        private LocalDateTime modifiedTime;
    }

    @Data
    private static class BeanTwo {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime createdTime;
        private LocalDateTime modifiedTime;
    }
}
