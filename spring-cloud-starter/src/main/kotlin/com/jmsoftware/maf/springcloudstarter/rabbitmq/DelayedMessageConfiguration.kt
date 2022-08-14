package com.jmsoftware.maf.springcloudstarter.rabbitmq

import com.jmsoftware.maf.common.util.logger
import org.springframework.amqp.core.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean

/**
 * # DelayedMessageConfiguration
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 8:12 PM
 * @see <a href='https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq'>Scheduling Messages with RabbitMQ</a>
 * @see <a href='https://github.com/rabbitmq/rabbitmq-delayed-message-exchange'>RabbitMQ Delayed Message Plugin</a>
 * @see <a href='https://www.rabbitmq.com/community-plugins.html'>Community Plugins</a>
 */
class DelayedMessageConfiguration {
    companion object {
        const val DELAYED_MESSAGE_QUEUE_NAME = "delayed-message.queue"
        const val DELAYED_MESSAGE_EXCHANGE_NAME = "delayed-message.exchange"
        const val DELAYED_MESSAGE_ROUTING_KEY = "delayed-message.routing-key"
        private val log = logger()
    }

    /**
     * Delayed message queue, which is durable, non-exclusive and non auto-delete.
     *
     * @return the delayed message queue
     */
    @Bean
    fun delayedMessageQueue(): Queue {
        return QueueBuilder
            .durable(DELAYED_MESSAGE_QUEUE_NAME)
            .build().apply {
                log.warn("Built delayed message queue: $this")
            }
    }

    /**
     * Delayed message exchange custom exchange.
     *
     * @return the custom exchange
     */
    @Bean
    fun delayedMessageExchange(): Exchange {
        return ExchangeBuilder
            .directExchange(DELAYED_MESSAGE_EXCHANGE_NAME)
            .delayed()
            // To use the Delayed Message Exchange you just need to declare an exchange providing
            // the "x-delayed-message" exchange type as follows
            .withArguments(mapOf("x-delayed-type" to "direct"))
            .build<Exchange>().apply {
                log.warn("Built delayed message exchange: $this")
            }
    }

    /**
     * Delayed message binding.
     *
     * @param delayedMessageQueue    the delayed message queue
     * @param delayedMessageExchange the delayed message exchange
     * @return the binding
     */
    @Bean
    fun delayedMessageBinding(
        @Qualifier("delayedMessageQueue") delayedMessageQueue: Queue?,
        @Qualifier("delayedMessageExchange") delayedMessageExchange: Exchange?
    ): Binding {
        return BindingBuilder
            .bind(delayedMessageQueue)
            .to(delayedMessageExchange)
            .with(DELAYED_MESSAGE_ROUTING_KEY)
            .noargs().apply {
                log.warn("Built delayed message binding: $this")
            }
    }
}
