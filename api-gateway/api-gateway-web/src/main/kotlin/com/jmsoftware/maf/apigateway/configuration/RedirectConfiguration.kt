package com.jmsoftware.maf.apigateway.configuration

import com.jmsoftware.maf.common.util.logger
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import java.net.URI

/**
 * # RedirectConfiguration
 *
 * Redirect Configuration.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:31 PM
 */
@Configuration
class RedirectConfiguration {
    companion object {
        private val log = logger()
    }

    @PostConstruct
    private fun postConstruct() {
        log.info("URL redirect service initialized.")
    }

    @Bean
    fun home(): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.GET("/")) {
            log.info("Redirect to Home page.")
            ServerResponse.temporaryRedirect(URI.create("/static/home.html")).build()
        }
    }

    @Bean
    fun doc(): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.GET("/doc")) {
            log.info("Redirect to Bootstrap Swagger API documentation.")
            ServerResponse.temporaryRedirect(URI.create("/doc.html#/home/en-US")).build()
        }
    }

    @Bean
    fun favicon(): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.GET("/favicon.ico")) {
            log.info("Redirect to favicon.")
            ServerResponse.temporaryRedirect(URI.create("/static/asset/favicon.ico")).build()
        }
    }
}
