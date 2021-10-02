package com.jmsoftware.maf.springcloudstarter.rabbitmq;

import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Description: RabbitmqConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 3/9/2021 11:31 AM
 * @see <a href='https://spring.io/guides/gs/messaging-rabbitmq/'>Messaging with RabbitMQ</a>
 * @see
 * <a href='https://thepracticaldeveloper.com/produce-and-consume-json-messages-with-spring-boot-amqp/'>Sending and receiving JSON messages with Spring Boot AMQP and RabbitMQ</a>
 **/
@Slf4j
@ConditionalOnClass({TopicExchange.class})
@Import({DelayedMessageConfiguration.class})
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
