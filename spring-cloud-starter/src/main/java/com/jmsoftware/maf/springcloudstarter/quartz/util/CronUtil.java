package com.jmsoftware.maf.springcloudstarter.quartz.util;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * CronUtil
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 3:52 PM
 */
public class CronUtil {
    private CronUtil() {
    }

    /**
     * Is valid boolean.
     *
     * @param cronExpression the cron expression
     * @return the boolean
     */
    public static boolean isValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * Gets invalid message.
     *
     * @param cronExpression the cron expression
     * @return the invalid message
     */
    public static String getInvalidMessage(String cronExpression) {
        try {
            new CronExpression(cronExpression);
            return null;
        } catch (ParseException pe) {
            return pe.getMessage();
        }
    }

    /**
     * Gets next execution.
     *
     * @param cronExpression the cron expression
     * @return the next execution
     */
    public static Date getNextExecution(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
