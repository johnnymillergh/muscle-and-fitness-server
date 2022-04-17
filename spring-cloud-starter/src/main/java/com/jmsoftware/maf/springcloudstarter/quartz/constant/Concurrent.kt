package com.jmsoftware.maf.springcloudstarter.quartz.constant

/**
 * # Concurrent
 *
 * Description: Concurrent, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:28 PM
 */
enum class Concurrent(val value: Byte) {
    /**
     * Disallow Concurrent
     */
    DISALLOW_CONCURRENT(0.toByte()),

    /**
     * concurrent
     */
    CONCURRENT(1.toByte());
}
