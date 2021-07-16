package com.jmsoftware.maf.springcloudstarter.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import static org.springframework.web.cors.CorsConfiguration.ALL;

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
@EnableWebSocketMessageBroker
@ConditionalOnClass({ServerEndpointExporter.class})
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
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

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/web-socket", "/ws")
                .setAllowedOriginPatterns(ALL)
                .withSockJS();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // In-memory message broker with one or more destinations for sending and receiving messages

        // There are two destination prefixes defined: topic and queue. They follow the convention that destinations
        // for messages to be carried on to all subscribed clients via the pub-sub model should be prefixed with
        // "topic".
        // On the other hand, destinations for private messages are typically prefixed by "queue".
        registry.enableSimpleBroker("/topic/", "/queue/");
        // Defines the prefix app that is used to filter destinations handled by methods annotated with
        // @MessageMapping which you will implement in a controller. The controller, after processing the message,
        // will send it to the broker.
        registry.setApplicationDestinationPrefixes("/app");

        // TODO: use RabbitMQ as broker instead of in-memory message broker
        // Use this for enabling a Full featured broker like RabbitMQ
        /*
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        */
    }
}
