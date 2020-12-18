package com.jmsoftware.maf.gateway.universal.configuration;

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
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/15/20 11:54 PM
 **/
@Slf4j
@Configuration
public class RedirectConfiguration {
    @PostConstruct
    private void postConstruct() {
        log.info("URL redirect service initialized.");
    }

    @Bean
    public RouterFunction<ServerResponse> home() {
        return route(GET("/"), request -> {
            log.info("Redirect to Home page.");
            return ServerResponse.temporaryRedirect(URI.create("/static/home.html")).build();
        });
    }

    @Bean
    public RouterFunction<ServerResponse> doc() {
        return route(GET("/doc"), request -> {
            log.info("Redirect to Bootstrap Swagger API documentation.");
            return ServerResponse.temporaryRedirect(URI.create("/doc.html")).build();
        });
    }

    @Bean
    public RouterFunction<ServerResponse> favicon() {
        return route(GET("/favicon.ico"), request -> {
            log.info("Redirect to favicon.");
            return ServerResponse.temporaryRedirect(URI.create("/static/icon/favicon.ico")).build();
        });
    }
}
