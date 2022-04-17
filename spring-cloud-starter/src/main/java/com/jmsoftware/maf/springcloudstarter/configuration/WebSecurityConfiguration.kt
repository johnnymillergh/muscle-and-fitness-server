package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
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
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 10:52 PM
 */
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
