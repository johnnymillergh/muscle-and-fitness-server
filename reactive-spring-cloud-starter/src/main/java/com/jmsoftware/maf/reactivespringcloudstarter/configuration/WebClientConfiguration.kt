package com.jmsoftware.maf.reactivespringcloudstarter.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

/**
 * Description: WebClientConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/14/2021 3:38 PM
 **/
@Slf4j
@RequiredArgsConstructor
public class WebClientConfiguration {
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
    public WebClient.Builder loadBalancedWebClientBuilder() {
        log.warn("Initial bean: '{}'", WebClient.Builder.class.getSimpleName());
        return WebClient.builder();
    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(false);
        factory.setConnectionProvider(ConnectionProvider.create("web-client-connection-provider"));
        factory.setLoopResources(LoopResources.create("web-client-loop"));
        return factory;
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
    public WebClient webClient(WebClient.Builder loadBalancedWebClientBuilder,
                               ReactorResourceFactory reactorResourceFactory) {
        return loadBalancedWebClientBuilder
                .clientConnector(new ReactorClientHttpConnector(reactorResourceFactory, client -> client
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .doOnConnected(doOnConnected -> {
                            doOnConnected.addHandlerLast(new ReadTimeoutHandler(10));
                            doOnConnected.addHandlerLast(new WriteTimeoutHandler(10));
                        })))
                .build();
    }
}
