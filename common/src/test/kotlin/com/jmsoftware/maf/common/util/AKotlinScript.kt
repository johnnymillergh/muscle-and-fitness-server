package com.jmsoftware.maf.common.util

import com.jmsoftware.maf.common.util.Slf4j.Companion.log

@Slf4j
internal object AKotlinScript

internal object AnotherKotlinScript

fun function1(aInteger: Int): Int {
    AKotlinScript.log.info("function1")
    return aInteger
}

fun function2() {
    AnotherKotlinScript.log.info("function2")
}
