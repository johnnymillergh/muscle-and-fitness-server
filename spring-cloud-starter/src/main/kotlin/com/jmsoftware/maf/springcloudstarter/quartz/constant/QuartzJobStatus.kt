package com.jmsoftware.maf.springcloudstarter.quartz.constant

/**
 * # QuartzJobStatus
 *
 * Description: QuartzJobStatus, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:32 PM
 */
enum class QuartzJobStatus(val value: Byte) {
    /**
     * Normal
     */
    NORMAL(0.toByte()),

    /**
     * Paused
     */
    PAUSE(1.toByte());
}
