package com.jmsoftware.maf.springcloudstarter.websocket

import com.jmsoftware.maf.common.websocket.WebSocketMessagePayload
import org.springframework.validation.annotation.Validated

/**
 * # ChatRoomService
 *
 * Description: ChatRoomService, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:03 AM
 */
@Validated
interface ChatRoomService {
    /**
     * Send message.
     *
     * @param payload the payload
     * @param headers the headers
     */
    fun sendMessage(payload: WebSocketMessagePayload, headers: Map<String, Any>)
}
