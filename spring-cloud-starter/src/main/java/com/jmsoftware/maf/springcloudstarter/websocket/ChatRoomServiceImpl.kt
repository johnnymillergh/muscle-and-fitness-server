package com.jmsoftware.maf.springcloudstarter.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.common.websocket.WebSocketMessagePayload
import com.jmsoftware.maf.common.websocket.WebSocketMessageResponse
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

/**
 * # ChatRoomServiceImpl
 *
 * Description: ChatRoomServiceImpl, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:04 AM
 */
@Service
class ChatRoomServiceImpl(
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val objectMapper: ObjectMapper
) : ChatRoomService {
    companion object {
        /**
         * `/public/broadcast` is not a valid topic destination
         *
         * RabbitMQ does not support slash (`/`) as a separator.
         *
         * @see
         * <a href='https://github.com/spring-guides/gs-messaging-stomp-websocket/issues/35#issuecomment-295789752'>Stomp client fails to connect to a dynamic controller destination that has a @DestinationVariable when I switch to use a StompBrokerRelay but it works with SimpleBroker</a>
         */
        const val BROADCAST_CHANNEL = "/topic/public-broadcast"
        val log = logger()
    }

    override fun sendMessage(payload: WebSocketMessagePayload, headers: Map<String, Any>) {
        val response = WebSocketMessageResponse()
        response.sender = payload.sender
        response.receiver = payload.receiver
        response.content = payload.content
        simpMessagingTemplate.convertAndSend(BROADCAST_CHANNEL, objectMapper.writeValueAsString(response))
        log.info("Got message from client. simpSessionId: ${headers["simpSessionId"]}, payload: $payload")
    }
}
