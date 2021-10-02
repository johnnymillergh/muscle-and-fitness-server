package com.jmsoftware.maf.authcenter.configuration;

import com.google.common.collect.Maps;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.text.CharSequenceUtil.format;
import static com.jmsoftware.maf.springcloudstarter.rabbitmq.DelayedMessageConfiguration.*;

/**
 * <h1>DelayedMessageSender</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 10/2/21 1:27 PM
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayedMessageSender {
    private final RabbitTemplate rabbitTemplate;
    private final MafProjectProperties mafProjectProperties;

    @Scheduled(fixedDelay = 2L, timeUnit = TimeUnit.MINUTES)
    public void sendMessage() {
        val message = format("{} - {}", this.mafProjectProperties.getProjectArtifactId(), LocalDateTime.now());
        val messageMap = Maps.<String, String>newHashMap();
        messageMap.put("message", message);
        this.rabbitTemplate.convertAndSend(
                DELAYED_MESSAGE_EXCHANGE_NAME,
                DELAYED_MESSAGE_ROUTING_KEY,
                messageMap,
                messagePostProcessor -> {
                    // Make it be consumed after 30 seconds
                    messagePostProcessor.getMessageProperties().setDelay(30 * 1000);
                    return messagePostProcessor;
                }
        );
        log.info("Sent a delayed message into queue: {}, messageMap: {}", DELAYED_MESSAGE_QUEUE_NAME, messageMap);
    }
}
