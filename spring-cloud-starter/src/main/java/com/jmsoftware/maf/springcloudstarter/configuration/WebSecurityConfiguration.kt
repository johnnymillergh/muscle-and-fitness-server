package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.annotation.PostConstruct

/**
 * <h1>WebSecurityConfiguration</h1>
 *
 *
 * Security handler configuration.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 */
@Slf4j
@EnableWebSecurity
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
    companion object {
        private val log = logger()
    }

    @PostConstruct
    fun postConstruct() {
        log.warn("Initial bean: `${WebSecurityConfiguration::class.java.simpleName}`")
    }

    public override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity) {
        // Disable Web Security.
        http.authorizeRequests().anyRequest().permitAll().and().csrf().disable().cors().disable()
    }

    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        log.warn("Initial bean: `${BCryptPasswordEncoder::class.java.simpleName}`")
        return BCryptPasswordEncoder()
    }
}
