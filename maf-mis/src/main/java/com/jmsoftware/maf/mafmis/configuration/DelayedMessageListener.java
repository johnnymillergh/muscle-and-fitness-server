package com.jmsoftware.maf.mafmis.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.jmsoftware.maf.springcloudstarter.rabbitmq.DelayedMessageConfiguration.DELAYED_MESSAGE_QUEUE_NAME;

/**
 * <h1>DelayedMessageListener</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 10/2/21 1:38 PM
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayedMessageListener {
    @RabbitListener(queues = DELAYED_MESSAGE_QUEUE_NAME)
    public void receiveMessage(final Message message) {
        log.info("Received message as a generic AMQP 'Message' wrapper: {}", message);
    }
}
