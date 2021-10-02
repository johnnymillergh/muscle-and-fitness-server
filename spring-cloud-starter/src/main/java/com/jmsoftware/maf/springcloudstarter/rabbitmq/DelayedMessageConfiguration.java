package com.jmsoftware.maf.springcloudstarter.rabbitmq;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

/**
 * <h1>DelayedMessageConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 10/2/21 12:03 PM
 * @see
 * <a href='https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq'>Scheduling Messages with RabbitMQ</a>
 * @see <a href='https://github.com/rabbitmq/rabbitmq-delayed-message-exchange'>RabbitMQ Delayed Message Plugin</a>
 * @see <a href='https://www.rabbitmq.com/community-plugins.html'>Community Plugins</a>
 **/
@Slf4j
public class DelayedMessageConfiguration {
    public static final String DELAYED_MESSAGE_QUEUE_NAME = "delayed-message.queue";
    public static final String DELAYED_MESSAGE_EXCHANGE_NAME = "delayed-message.exchange";
    public static final String DELAYED_MESSAGE_ROUTING_KEY = "delayed-message.routing-key";

    /**
     * Delayed message queue, which is durable, non-exclusive and non auto-delete.
     *
     * @return the delayed message queue
     */
    @Bean
    public Queue delayedMessageQueue() {
        val delayedMessageQueue = QueueBuilder.durable(DELAYED_MESSAGE_QUEUE_NAME).build();
        log.warn("Built delayed message queue: {}", delayedMessageQueue);
        return delayedMessageQueue;
    }

    /**
     * Delayed message exchange custom exchange.
     *
     * @return the custom exchange
     */
    @Bean
    public Exchange delayedMessageExchange() {
        val arguments = Maps.<String, Object>newHashMap();
        // To use the Delayed Message Exchange you just need to declare an exchange providing
        // the "x-delayed-message" exchange type as follows
        arguments.put("x-delayed-type", "direct");
        val delayedMessageExchange = ExchangeBuilder
                .directExchange(DELAYED_MESSAGE_EXCHANGE_NAME)
                .delayed()
                .withArguments(arguments)
                .build();
        log.warn("Built delayed message exchange: {}", delayedMessageExchange);
        return delayedMessageExchange;
    }

    /**
     * Delayed message binding.
     *
     * @param delayedMessageQueue    the delayed message queue
     * @param delayedMessageExchange the delayed message exchange
     * @return the binding
     */
    @Bean
    public Binding delayedMessageBinding(@Qualifier("delayedMessageQueue") Queue delayedMessageQueue,
                                         @Qualifier("delayedMessageExchange") Exchange delayedMessageExchange) {
        val binding = BindingBuilder
                .bind(delayedMessageQueue)
                .to(delayedMessageExchange)
                .with(DELAYED_MESSAGE_ROUTING_KEY)
                .noargs();
        log.warn("Built delayed message binding: {}", binding);
        return binding;
    }
}
