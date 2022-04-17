package com.jmsoftware.maf.springcloudstarter.websocket

import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.cors.CorsConfiguration.ALL
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

/**
 * # WebSocketMessageBrokerConfiguration
 *
 * Description: WebSocketMessageBrokerConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:11 AM
 */
class WebSocketMessageBrokerConfiguration(
    private val rabbitProperties: RabbitProperties
) : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/web-socket", "/websocket", "/ws")
            .setAllowedOriginPatterns(ALL)
            .withSockJS()
    }

    /**
     * There are two destination prefixes defined: topic and queue. They follow the convention that destinations for messages to be carried on to all subscribed clients via the pub-sub model should be prefixed with "topic". On the other hand, destinations for private messages are typically prefixed by "queue"
     *
     * registry.enableSimpleBroker("/topic/", "/queue/");`
     *
     * Defines the prefix app that is used to filter destinations handled by methods annotated with `@MessageMapping` which you will implement in a controller. The controller, after processing the message, will send it to the broker.
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // Use RabbitMQ as broker instead of in-memory message broker, enabling a full-featured broker like RabbitMQ
        registry
            .enableStompBrokerRelay(
                "/topic/",
                "/queue/"
            )
            // We need to specify the host but not the port, cuz we're using the default STOMP relay port, which is "61613"
            .setRelayHost(rabbitProperties.host)
            .setClientLogin(rabbitProperties.username)
            .setClientPasscode(rabbitProperties.password)
            .setSystemLogin(rabbitProperties.username)
            .setSystemPasscode(rabbitProperties.password)
        registry.setApplicationDestinationPrefixes("/app")
    }
}
