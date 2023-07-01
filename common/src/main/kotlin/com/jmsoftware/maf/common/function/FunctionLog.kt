package com.jmsoftware.maf.common.function

import com.jmsoftware.maf.common.constant.ISO_8601_DATETIME_FORMAT
import com.jmsoftware.maf.common.function.FunctionLog.log
import com.jmsoftware.maf.common.util.logger
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Function

/**
 * # FunctionLog
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 2:55 PM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-5'>Java 函数式编程最佳实践 - 赋予函数记录日志能力</a>
 */
private object FunctionLog {
    val log = logger()
}

/**
 * Log function r.
 *
 * @param <T>      the type parameter
 * @param <R>      the type parameter
 * @param function the function
 * @param t        the t
 * @param tag      the tag
 * @return the r
 */
fun <T, R> logFunction(function: Function<T, R>, t: T, tag: String?): R {
    log.info(
        "[$tag], parameter = ${t.toString()}, requestTime = ${
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(ISO_8601_DATETIME_FORMAT))
        }"
    )
    val start = Instant.now()
    val apply = function.apply(t)
    val end = Instant.now()
    log.info("[$tag], return = ${apply.toString()}, elapsed time = ${Duration.between(start, end).toMillis()} ms")
    return apply
}
