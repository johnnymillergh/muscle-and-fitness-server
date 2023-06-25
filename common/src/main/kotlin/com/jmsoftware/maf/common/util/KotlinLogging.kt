package com.jmsoftware.maf.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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

class LoggerDelegate : ReadOnlyProperty<Any?, Logger> {
    companion object {
        private fun <T> createLogger(clazz: Class<T>): Logger {
            return LoggerFactory.getLogger(clazz)
        }
    }

    private var logger: Logger? = null

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Logger {
        if (logger == null) {
            logger = createLogger(thisRef!!.javaClass)
        }
        return logger!!
    }
}

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Suppress("UnusedReceiverParameter")
annotation class Slf4j {
    companion object {
        inline val <reified T> T.log: Logger get() = LoggerFactory.getLogger(T::class.java)
    }
}
