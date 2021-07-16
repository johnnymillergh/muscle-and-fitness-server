package com.jmsoftware.maf.springcloudstarter.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
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
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@ConditionalOnClass({ServerEndpointExporter.class, RabbitTemplate.class})
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    private final RabbitProperties rabbitProperties;

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
        registry.addEndpoint("/web-socket", "/websocket", "/ws")
                .setAllowedOriginPatterns(ALL)
                .withSockJS();
    }

    /**
     * {@inheritDoc}
     *
     * <p>There are two destination prefixes defined: topic and queue. They follow the convention that destinations
     * for messages to be carried on to all subscribed clients via the pub-sub model should be prefixed with &quot;
     * topic&quot;. On the other hand, destinations for private messages are typically prefixed by &quot;queue&quot;
     * .</p>
     * <pre><code class='language-java' lang='java'>registry.enableSimpleBroker(&quot;/topic/&quot;, &quot;/queue/&quot;);
     * </code></pre>
     * <p>Defines the prefix app that is used to filter destinations handled by methods annotated with
     * <code>@MessageMapping</code> which you will implement in a controller. The controller, after processing the
     * message, will send it to the broker.</p>
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Use RabbitMQ as broker instead of in-memory message broker, enabling a full featured broker like RabbitMQ
        registry.enableStompBrokerRelay("/topic/", "/queue/")
                // We need to specify the host but not the port, cuz we're using the default STOMP relay port, which
                // is "61613"
                .setRelayHost(this.rabbitProperties.getHost())
                .setClientLogin(this.rabbitProperties.getUsername())
                .setClientPasscode(this.rabbitProperties.getPassword())
                .setSystemLogin(this.rabbitProperties.getUsername())
                .setSystemPasscode(this.rabbitProperties.getPassword());
        registry.setApplicationDestinationPrefixes("/app");
    }
}
