package com.jmsoftware.maf.apigateway.security.configuration;

import com.jmsoftware.maf.apigateway.security.impl.*;
import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

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

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                ServerAuthenticationEntryPoint serverAuthenticationEntryPoint,
                                                ServerAccessDeniedHandler serverAccessDeniedHandler,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager,
                                                ServerSecurityContextRepository serverSecurityContextRepository,
                                                ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager) {
        if (mafConfiguration.getWebSecurityDisabled()) {
            log.warn("Web security was disabled.");
            return http
                    .cors().disable()
                    .csrf().disable()
                    .build();
        }
        return http
                .cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(serverAuthenticationEntryPoint)
                .accessDeniedHandler(serverAccessDeniedHandler)
                .and()
                // Authentication
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(serverSecurityContextRepository)
                .authorizeExchange()
                .pathMatchers(mafConfiguration.flattenIgnoredUrls()).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                // Authorization
                .anyExchange().access(reactiveAuthorizationManager)
                .and()
                .build();
    }

    @Bean
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint() {
        return new ServerAuthenticationEntryPointImpl();
    }

    @Bean
    public ServerAccessDeniedHandler serverAccessDeniedHandler() {
        return new GatewayServerAccessDeniedHandlerImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager() {
        return new RbacReactiveAuthorizationManagerImpl(mafConfiguration);
    }

    @Bean
    public ServerSecurityContextRepository serverSecurityContextRepository(ReactiveAuthenticationManager reactiveAuthenticationManager) {
        return new JwtReactiveServerSecurityContextRepositoryImpl(mafConfiguration, reactiveAuthenticationManager);
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new JwtReactiveAuthenticationManagerImpl();
    }
}
