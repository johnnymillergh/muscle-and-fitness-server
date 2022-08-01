package com.jmsoftware.maf.springcloudstarter.rabbitmq

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.rabbitmq.DelayedMessageConfiguration
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

/**
 * # RabbitmqConfiguration
 *
 * Description: RabbitmqConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 8:14 PM
 * @see RabbitAutoConfiguration
 * @see <a href='https://spring.io/guides/gs/messaging-rabbitmq/'>Messaging with RabbitMQ</a>
 * @see <a href='https://thepracticaldeveloper.com/produce-and-consume-json-messages-with-spring-boot-amqp/'>Sending and receiving JSON messages with Spring Boot AMQP and RabbitMQ</a>
 */
@ConditionalOnClass(TopicExchange::class)
@Import(
    DelayedMessageConfiguration::class
)
class RabbitmqConfiguration {
    companion object {
        private val log = logger()
    }

    @Bean
    fun messageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

    /**
     * Rabbit listener container factory.
     *
     * @param rabbitProperties  the rabbit properties
     * @param configurer        the configurer
     * @param connectionFactory the connection factory
     * @return the simple rabbit listener container factory
     * @see RabbitAnnotationDrivenConfiguration.directRabbitListenerContainerFactory
     */
    @Primary
    @Bean(name = ["rabbitListenerContainerFactory"])
    fun rabbitListenerContainerFactory(
        rabbitProperties: RabbitProperties,
        configurer: SimpleRabbitListenerContainerFactoryConfigurer,
        connectionFactory: ConnectionFactory
    ): SimpleRabbitListenerContainerFactory {
        val simpleContainer = rabbitProperties.listener.simple
        simpleContainer.concurrency = 2
        simpleContainer.maxConcurrency = 4
        log.warn("Overriding `rabbitListenerContainerFactory` with custom configuration, concurrency: ${simpleContainer.concurrency}, maxConcurrency: ${simpleContainer.maxConcurrency}")
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrentConsumers(2)
        factory.setMaxConcurrentConsumers(4)
        configurer.configure(factory, connectionFactory)
        return factory
    }
}
