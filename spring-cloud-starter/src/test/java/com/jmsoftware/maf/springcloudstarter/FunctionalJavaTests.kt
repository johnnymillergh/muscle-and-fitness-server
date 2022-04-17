package com.jmsoftware.maf.springcloudstarter

import cn.hutool.json.JSONUtil
import com.jmsoftware.maf.common.util.logger
import fj.Show
import fj.data.List
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

/**
 * # FunctionalJavaTests
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:28 AM
 */
@Execution(ExecutionMode.CONCURRENT)
internal class FunctionalJavaTests {
    companion object {
        var log = logger()
    }

    @Test
    fun testListMap() {
        val arraylist = List.list(1, 2, 3).map { i: Int -> i + 42 }
        Show.stringShow.println("Show arraylist in for-each loop:")
        for (integer in arraylist) {
            Show.intShow.println(integer)
        }
        Show.stringShow.print("listShow: ")
        Show.listShow(Show.intShow).println(arraylist)
        log.info("Serialized JSON array of arraylist: ${JSONUtil.parse(arraylist)}")
        assertEquals(45, arraylist.index(2))
    }
}
