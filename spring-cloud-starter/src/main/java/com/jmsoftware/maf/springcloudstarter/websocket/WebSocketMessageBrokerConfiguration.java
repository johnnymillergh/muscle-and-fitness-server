package com.jmsoftware.maf.springcloudstarter.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * Description: WebSocketMessageBrokerConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/6/2021 5:09 PM
 **/
@RequiredArgsConstructor
public class WebSocketMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer {
    private final RabbitProperties rabbitProperties;

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
