package com.jmsoftware.maf.common.websocket

import java.time.LocalDateTime

/**
 * # WebSocketMessageResponse
 *
 * Description: WebSocketMessagePayload, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:41 PM
 */
class WebSocketMessageResponse {
    var timestamp: LocalDateTime = LocalDateTime.now()
    var sender: String? = null
    var receiver: String? = null
    var content: Any? = null
}
