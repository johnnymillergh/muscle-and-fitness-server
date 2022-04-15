package com.jmsoftware.maf.springcloudstarter.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * Description: RabbitmqConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 3/9/2021 11:31 AM
 * @see RabbitAutoConfiguration
 * @see <a href='https://spring.io/guides/gs/messaging-rabbitmq/'>Messaging with RabbitMQ</a>
 * @see
 * <a href='https://thepracticaldeveloper.com/produce-and-consume-json-messages-with-spring-boot-amqp/'>Sending and receiving JSON messages with Spring Boot AMQP and RabbitMQ</a>
 **/
@Slf4j
@ConditionalOnClass({TopicExchange.class})
@Import({DelayedMessageConfiguration.class})
public class RabbitmqConfiguration {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Rabbit listener container factory.
     *
     * @param rabbitProperties  the rabbit properties
     * @param configurer        the configurer
     * @param connectionFactory the connection factory
     * @return the simple rabbit listener container factory
     * @see RabbitAnnotationDrivenConfiguration#directRabbitListenerContainerFactory(org.springframework.boot.autoconfigure.amqp.DirectRabbitListenerContainerFactoryConfigurer, org.springframework.amqp.rabbit.connection.ConnectionFactory)
     */
    @Primary
    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            RabbitProperties rabbitProperties,
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory
    ) {
        val simpleContainer = rabbitProperties.getListener().getSimple();
        simpleContainer.setConcurrency(2);
        simpleContainer.setMaxConcurrency(4);
        log.warn(
                "Overriding `rabbitListenerContainerFactory` with custom configuration, concurrency: {}, "
                        + "maxConcurrency: {}", simpleContainer.getConcurrency(), simpleContainer.getMaxConcurrency()
        );
        val factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(4);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
