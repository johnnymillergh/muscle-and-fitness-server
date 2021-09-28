package com.jmsoftware.maf.springcloudstarter.function;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import static com.jmsoftware.maf.common.constant.UniversalDateTime.DATE_TIME_FORMAT;

/**
 * <h1>FunctionLog</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 9/28/21 8:57 AM
 * @see <a href='https://juejin.cn/post/6892298625058078727#heading-5'>Java 函数式编程最佳实践 - 赋予函数记录日志能力</a>
 **/
@Slf4j
public class FunctionLog {
    public static <T, R> R logFunction(Function<T, R> function, T t, String tag) {
        long startTime = System.currentTimeMillis();
        log.info("[{}], parameter = {}, requestTime = {}", tag, t.toString(),
                 LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        R apply = function.apply(t);
        long endTime = System.currentTimeMillis();
        log.info("[{}], return = {}, elapsed time = {} ms", tag, apply.toString(), endTime - startTime);
        return apply;
    }
}
