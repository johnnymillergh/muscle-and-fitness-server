package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import jakarta.annotation.PostConstruct

/**
 * # WebSecurityConfiguration
 *
 * Security handler configuration.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:20 PM
 */
@EnableWebSecurity
class WebSecurityConfiguration {
    companion object {
        private val log = logger()
    }

    @PostConstruct
    fun postConstruct() {
        log.warn("Initial bean: `${WebSecurityConfiguration::class.java.simpleName}`")
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()
            .csrf()
            .disable()
            .cors()
            .disable()
            .build()

    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        log.warn("Initial bean: `${BCryptPasswordEncoder::class.java.simpleName}`")
        return BCryptPasswordEncoder()
    }
}
