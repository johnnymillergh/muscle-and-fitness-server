package com.jmsoftware.maf.gateway.security;

import com.jmsoftware.maf.muscleandfitnessserverreactivespringbootstarter.configuration.MafConfiguration;
import com.jmsoftware.maf.muscleandfitnessserverreactivespringbootstarter.filter.AccessLogFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Description: WebFluxSecurityConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/18/2020 3:23 PM
 * @see
 * <a href='https://docs.spring.io/spring-security/site/docs/5.3.6.RELEASE/reference/html5/#reactive-applications'>Spring Secirity Reference - Reactive Applications</a>
 * @see
 * <a href='https://www.devglan.com/spring-security/spring-security-webflux-jwt'>Securing Spring WebFlux Reactive APIs with JWT Auth</a>
 * @see
 * <a href='https://blog.csdn.net/tiancao222/article/details/104375924'>SpringCloud Gateway 整合 Spring Security Webflux 的关键点（痛点解析），及示例项目</a>
 **/
@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebFluxSecurityConfiguration {
    private final MafConfiguration mafConfiguration;
    private final JwtReactiveAuthenticationManager reactiveAuthenticationManager;
    private final RbacReactiveAuthorizationManager reactiveAuthorizationManager;
    private final JwtReactiveServerSecurityContextRepository securityContextRepository;
    private final ServerAuthenticationEntryPointImpl serverAuthenticationEntryPoint;
    private final GatewayServerAccessDeniedHandler serverAccessDeniedHandler;
    private final AccessLogFilter accessLogFilter;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(serverAuthenticationEntryPoint)
                .accessDeniedHandler(serverAccessDeniedHandler)
                .and()
                .addFilterBefore(accessLogFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                // Authentication
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(mafConfiguration.flattenIgnoredUrls()).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                // Authorization
                .anyExchange().access(reactiveAuthorizationManager)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
