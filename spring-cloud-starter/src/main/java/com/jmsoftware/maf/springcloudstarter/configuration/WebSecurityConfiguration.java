package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * <h1>WebSecurityConfiguration</h1>
 * <p>
 * Security handler configuration.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Slf4j
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @PostConstruct
    void postConstruct() {
        log.warn("Initial bean: '{}'", WebSecurityConfiguration.class.getSimpleName());
    }

    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable Web Security.
        http.authorizeRequests().anyRequest().permitAll().and().csrf().disable().cors().disable();
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        log.warn("Initial bean: '{}'", BCryptPasswordEncoder.class.getSimpleName());
        return new BCryptPasswordEncoder();
    }
}
