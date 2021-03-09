package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: RabbitmqConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 3/9/2021 11:31 AM
 **/
@Slf4j
@Configuration
public class RabbitmqConfiguration {
    public final String topicExchangeName;

    public RabbitmqConfiguration(MafProjectProperty mafProjectProperty) {
        topicExchangeName = String.format("%s-topic-exchange", mafProjectProperty.getProjectParentArtifactId());
    }

    @Bean
    TopicExchange topicExchange() {
        val exchange = new TopicExchange(topicExchangeName);
        log.info("Created topic exchange: {}", exchange);
        return exchange;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
