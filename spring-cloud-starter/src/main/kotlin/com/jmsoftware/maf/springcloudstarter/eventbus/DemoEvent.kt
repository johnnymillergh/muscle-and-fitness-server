package com.jmsoftware.maf.springcloudstarter.eventbus

import cn.hutool.core.util.IdUtil

/**
 * # DemoEvent
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/25/22 1:18 PM
 **/
data class DemoEvent(
    val id: String = IdUtil.nanoId(),
    val name: String = DemoEvent::class.java.simpleName
)
