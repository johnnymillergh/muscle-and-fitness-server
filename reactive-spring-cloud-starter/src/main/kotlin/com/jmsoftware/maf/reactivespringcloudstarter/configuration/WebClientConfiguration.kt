package com.jmsoftware.maf.reactivespringcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.client.reactive.ReactorResourceFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.resources.LoopResources

/**
 * # WebClientConfiguration
 *
 * Description: WebClientConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 7:54 AM
 */
class WebClientConfiguration {
    companion object {
        private val log = logger()
    }

    /**
     * Load balanced web client builder.
     *
     * @return the web client builder
     * @see
     * <a href='https://spring.io/blog/2020/03/25/spring-tips-spring-cloud-loadbalancer#the-code-loadbalanced-code-annotation'>Spring Tips: Spring Cloud Loadbalancer</a>
     * @see
     * <a href='https://piotrminkowski.com/2018/05/04/reactive-microservices-with-spring-webflux-and-spring-cloud/'>Reactive Microservices with Spring WebFlux and Spring Cloud</a>
     */
    @Bean
    @LoadBalanced
    fun loadBalancedWebClientBuilder(): WebClient.Builder {
        log.warn("Initial bean: `${WebClient.Builder::class.java.simpleName}`")
        return WebClient.builder()
    }

    @Bean
    fun reactorResourceFactory(): ReactorResourceFactory {
        return ReactorResourceFactory().also {
            it.isUseGlobalResources = false
            it.connectionProvider = ConnectionProvider.create("web-client-connection-provider")
            it.loopResources = LoopResources.create("web-client-loop")
        }
    }

    /**
     * Configure web client.
     *
     * @param reactorResourceFactory the reactor resource factory
     * @return the web client
     * @see
     * <a href='https://spring.getdocs.org/en-US/spring-cloud-docs/spring-cloud-commons/cloud-native-applications/spring-cloud-commons:-common-abstractions/loadbalanced-webclient.html#loadbalanced-webclient'>Spring WebFlux <code>WebClient</code> as a Load Balancer Client</a>
     */
    @Bean
    fun webClient(
        loadBalancedWebClientBuilder: WebClient.Builder,
        reactorResourceFactory: ReactorResourceFactory
    ): WebClient {
        return loadBalancedWebClientBuilder
            .clientConnector(ReactorClientHttpConnector(reactorResourceFactory) { client: HttpClient ->
                client
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .doOnConnected { doOnConnected: Connection ->
                        doOnConnected.addHandlerLast(ReadTimeoutHandler(10))
                        doOnConnected.addHandlerLast(WriteTimeoutHandler(10))
                    }
            }).build()
    }
}
