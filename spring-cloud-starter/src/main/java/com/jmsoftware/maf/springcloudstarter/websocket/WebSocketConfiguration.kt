package com.jmsoftware.maf.springcloudstarter.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.util.logger
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.server.standard.ServerEndpointExporter

/**
 * Description: WebSocketConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:11 AM
 * @see <a href='https://www.toptal.com/java/stomp-spring-boot-websocket'>Using Spring Boot for WebSocket Implementation with STOMP</a>
 * @see <a href='http://jmesnil.net/stomp-websocket/doc/'>STOMP Over WebSocket</a>
 * @see <a href='https://ordina-jworks.github.io/event-driven/2020/06/30/user-feedback-websockets.html'>ENABLING USER FEEDBACK WITH WEBSOCKETS ON RABBITMQ AND SPRING CLOUD</a>
 */
@EnableWebSocketMessageBroker
@Import(WebSocketMessageBrokerConfiguration::class)
@ConditionalOnClass(ServerEndpointExporter::class, RabbitTemplate::class)
class WebSocketConfiguration {
    companion object {
        private val log = logger()
    }

    @Bean
    fun greetingController(chatRoomService: ChatRoomService): ChatRoomController {
        log.warn("Initial bean: `${ChatRoomController::class.java.simpleName}`")
        return ChatRoomController(chatRoomService)
    }

    @Bean
    fun chatRoomService(
        simpMessagingTemplate: SimpMessagingTemplate,
        objectMapper: ObjectMapper
    ): ChatRoomService {
        log.warn("Initial bean: `${ChatRoomService::class.java.simpleName}`")
        return ChatRoomServiceImpl(simpMessagingTemplate, objectMapper)
    }
}
