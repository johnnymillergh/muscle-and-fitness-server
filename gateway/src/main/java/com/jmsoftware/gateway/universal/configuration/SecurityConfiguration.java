package com.jmsoftware.gateway.universal.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * <h1>SecurityConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/12/20 10:13 AM
 **/
@EnableWebFluxSecurity
public class SecurityConfiguration {
    private static final String[] excludedAuth = {
            "/auth/login",
            "/auth/logout",
            "/health",
            "/api/socket/**"
    };

    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange()
                .pathMatchers(excludedAuth).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                // 启动页面表单登陆,spring security 内置了一个登陆页面/login
                .formLogin()
                // 必须支持跨域
                .and().csrf().disable()
                .logout().disable();
        return http.build();
    }
}
