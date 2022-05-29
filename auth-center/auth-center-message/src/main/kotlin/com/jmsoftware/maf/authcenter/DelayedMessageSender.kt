package com.jmsoftware.maf.authcenter

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.rabbitmq.DelayedMessageConfiguration
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * # DelayedMessageSender
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 12:55 PM
 */
@Component
class DelayedMessageSender(
    private val rabbitTemplate: RabbitTemplate,
    private val mafProjectProperties: MafProjectProperties
) {
    companion object {
        private val log = logger()
    }

    @Scheduled(fixedDelay = 2L, timeUnit = TimeUnit.MINUTES)
    fun sendMessage() {
        val message = "${mafProjectProperties.projectArtifactId} - ${LocalDateTime.now()}"
        val messageMap = mapOf("message" to message)
        rabbitTemplate.convertAndSend(
            DelayedMessageConfiguration.DELAYED_MESSAGE_EXCHANGE_NAME,
            DelayedMessageConfiguration.DELAYED_MESSAGE_ROUTING_KEY,
            messageMap
        ) { messagePostProcessor: Message ->
            // Make it be consumed after 30 seconds
            messagePostProcessor.messageProperties.delay = 30 * 1000
            messagePostProcessor
        }
        log.info("Sent a delayed message into queue: ${DelayedMessageConfiguration.DELAYED_MESSAGE_QUEUE_NAME}, messageMap: $messageMap")
    }
}
