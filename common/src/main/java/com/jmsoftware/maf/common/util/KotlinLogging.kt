package com.jmsoftware.maf.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * # KotlinLogging
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 4/10/22 4:47 PM
 * @see <a href='https://www.reddit.com/r/Kotlin/comments/8gbiul/slf4j_loggers_in_3_ways/'>SLF4J loggers in 3 ways</a>
 **/
@Suppress("unused")
inline fun <reified T> T.logger(): Logger {
    return if (T::class.isCompanion) {
        LoggerFactory.getLogger(T::class.java.enclosingClass)
    } else {
        LoggerFactory.getLogger(T::class.java)
    }
}
