package com.jmsoftware.maf.authcenter.universal.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <h1>WebSecurityConfiguration</h1>
 * <p>
 * Security handler configuration.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable web security.
        http.cors()
                // Disable CSRF (Cross-site request forgery)
                .and().csrf().disable()
                // Disable form login to use custom login
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests().anyRequest().permitAll();
    }
}
