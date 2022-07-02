package com.jmsoftware.maf.springcloudstarter.eventbus

import cn.hutool.core.util.IdUtil.nanoId
import java.time.LocalDateTime
import java.time.LocalDateTime.now

/**
 * # EventButReadyEvent
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/25/22 1:18 PM
 **/
data class EventButReadyEvent(
    val id: String = nanoId(),
    val timestamp: LocalDateTime = now()
)
