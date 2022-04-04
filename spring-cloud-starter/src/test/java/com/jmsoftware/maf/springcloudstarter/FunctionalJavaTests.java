package com.jmsoftware.maf.springcloudstarter;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static fj.Show.*;
import static fj.data.List.list;

/**
 * <h1>FunctionalJavaTests</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 11:00 PM
 **/
@Slf4j
@Execution(ExecutionMode.CONCURRENT)
class FunctionalJavaTests {
    @Test
    void testListMap() {
        val arraylist = list(1, 2, 3).map(i -> i + 42);
        stringShow.println("Show arraylist in for-each loop:");
        for (Integer integer : arraylist) {
            intShow.println(integer);
        }
        stringShow.print("listShow: ");
        listShow(intShow).println(arraylist);
        log.info("Serialized JSON array of arraylist: {}", JSONUtil.parse(arraylist));
        Assertions.assertEquals(45, arraylist.index(2));
    }
}
