package com.jmsoftware.maf.mafmis

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.rabbitmq.DelayedMessageConfiguration
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

/**
 * # DelayedMessageListener
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:19 PM
 */
@Component
class DelayedMessageListener {
    companion object {
        private val log = logger()
    }

    @RabbitListener(queues = [DelayedMessageConfiguration.DELAYED_MESSAGE_QUEUE_NAME])
    fun receiveMessage(message: Message) {
        log.info("Received message as a generic AMQP 'Message' wrapper: $message")
    }
}
