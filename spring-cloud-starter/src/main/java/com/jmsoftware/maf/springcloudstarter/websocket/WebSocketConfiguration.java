package com.jmsoftware.maf.springcloudstarter.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Description: WebSocketConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/15/2021 12:15 AM
 * @see
 * <a href='https://www.toptal.com/java/stomp-spring-boot-websocket'>Using Spring Boot for WebSocket Implementation with STOMP</a>
 * @see <a href='http://jmesnil.net/stomp-websocket/doc/'>STOMP Over WebSocket</a>
 * @see
 * <a href='https://ordina-jworks.github.io/event-driven/2020/06/30/user-feedback-websockets.html'>ENABLING USER FEEDBACK WITH WEBSOCKETS ON RABBITMQ AND SPRING CLOUD</a>
 **/
@Slf4j
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Import({WebSocketMessageBrokerConfiguration.class})
@ConditionalOnClass({ServerEndpointExporter.class, RabbitTemplate.class})
public class WebSocketConfiguration {
    @Bean
    public ChatRoomController greetingController(ChatRoomService chatRoomService) {
        log.warn("Initial bean: '{}'", ChatRoomController.class.getSimpleName());
        return new ChatRoomController(chatRoomService);
    }

    @Bean
    public ChatRoomService chatRoomService(SimpMessagingTemplate simpMessagingTemplate,
                                           ObjectMapper objectMapper) {
        log.warn("Initial bean: '{}'", ChatRoomService.class.getSimpleName());
        return new ChatRoomServiceImpl(simpMessagingTemplate, objectMapper);
    }
}
