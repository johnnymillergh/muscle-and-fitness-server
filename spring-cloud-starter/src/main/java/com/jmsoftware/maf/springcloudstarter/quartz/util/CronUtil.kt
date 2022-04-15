package com.jmsoftware.maf.springcloudstarter.quartz.util

import org.quartz.CronExpression
import java.text.ParseException
import java.util.*

/**
 * Is valid boolean.
 *
 * @param cronExpression the cron expression
 * @return the boolean
 */
fun validateCronExp(cronExpression: String): Boolean {
    return CronExpression.isValidExpression(cronExpression)
}

/**
 * Gets invalid message.
 *
 * @param cronExpression the cron expression
 * @return the invalid message
 */
fun getInvalidMessage(cronExpression: String): String? {
    return try {
        CronExpression(cronExpression)
        null
    } catch (pe: ParseException) {
        pe.message
    }
}

/**
 * Gets next execution.
 *
 * @param cronExpression the cron expression
 * @return the next execution
 */
fun getNextExecution(cronExpression: String): Date {
    return try {
        val cron = CronExpression(cronExpression)
        cron.getNextValidTimeAfter(Date(System.currentTimeMillis()))
    } catch (e: ParseException) {
        throw IllegalArgumentException(e.message)
    }
}
