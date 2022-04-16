package com.jmsoftware.maf.springcloudstarter.websocket

import com.jmsoftware.maf.common.websocket.WebSocketMessagePayload
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

/**
 * # ChatRoomController
 *
 * Description: ChatRoomController, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:02 AM
 */
@Controller
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {
    @MessageMapping("/send-message")
    fun sendMessage(@Payload payload: WebSocketMessagePayload, @Headers headers: Map<String, Any>) {
        chatRoomService.sendMessage(payload, headers)
    }
}
