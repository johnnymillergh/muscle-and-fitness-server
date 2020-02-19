package com.jmsoftware.gateway.universal.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.PostConstruct;
import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * <h1>RedirectConfiguration</h1>
 * <p>
 * Redirect Configuration.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 2/15/20 11:54 PM
 **/
@Slf4j
@Configuration
public class RedirectConfiguration {
    @PostConstruct
    public void postConstruct() {
        log.info("URL redirect service initialized.");
    }

    @Bean
    public RouterFunction<ServerResponse> home() {
        return route(GET("/home"), request -> {
            log.info("Redirect to Home page.");
            return ServerResponse.temporaryRedirect(URI.create("/static/home.html")).build();
        });
    }

    @Bean
    public RouterFunction<ServerResponse> doc() {
        return route(GET("/doc"), request -> {
            log.info("Redirect to Bootstrap Swagger API documentation.");
            return ServerResponse.temporaryRedirect(URI.create("/doc.html?cache=1&lang=en")).build();
        });
    }

    @Bean
    public RouterFunction<ServerResponse> favicon() {
        return route(GET("/webjars/bycdao-ui/images/api.ico"), request -> {
            log.info("Redirect to Bootstrap Swagger API documentation.");
            return ServerResponse.temporaryRedirect(URI.create("/static/icon/favicon.ico")).build();
        });
    }
}
