package com.jmsoftware.maf.springcloudstarter.rabbitmq;

import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * Description: RabbitmqConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 3/9/2021 11:31 AM
 **/
@Slf4j
@ConditionalOnClass({TopicExchange.class})
public class RabbitmqConfiguration {
    public final String topicExchangeName;

    public RabbitmqConfiguration(MafProjectProperties mafProjectProperties) {
        this.topicExchangeName = String.format("%s-topic-exchange", mafProjectProperties.getProjectParentArtifactId());
    }

    @Bean
    TopicExchange topicExchange() {
        val exchange = new TopicExchange(this.topicExchangeName);
        log.info("Created topic exchange: {}", exchange);
        return exchange;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
