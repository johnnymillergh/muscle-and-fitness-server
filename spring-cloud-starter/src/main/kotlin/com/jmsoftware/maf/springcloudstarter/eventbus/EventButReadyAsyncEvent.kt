package com.jmsoftware.maf.springcloudstarter.eventbus

import cn.hutool.core.util.IdUtil.nanoId
import java.time.LocalDateTime
import java.time.LocalDateTime.now

/**
 * # EventButReadyAsyncEvent
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 7/2/22 9:32 AM
 **/
data class EventButReadyAsyncEvent(
    val id: String = nanoId(),
    val timestamp: LocalDateTime = now()
)
