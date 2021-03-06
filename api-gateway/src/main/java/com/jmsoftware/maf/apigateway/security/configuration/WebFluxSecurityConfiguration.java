package com.jmsoftware.maf.apigateway.security.configuration;

import cn.hutool.core.util.BooleanUtil;
import com.google.common.collect.Lists;
import com.jmsoftware.maf.apigateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.apigateway.security.impl.*;
import com.jmsoftware.maf.reactivespringcloudstarter.configuration.MafConfiguration;
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
    private final AuthCenterRemoteApi authCenterRemoteApi;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                ServerAuthenticationEntryPoint serverAuthenticationEntryPoint,
                                                ServerAccessDeniedHandler serverAccessDeniedHandler,
                                                ServerSecurityContextRepository serverSecurityContextRepository,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager,
                                                ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager) {
        if (BooleanUtil.isFalse(this.mafConfiguration.getWebSecurityEnabled())) {
            log.warn("Web security was disabled.");
            return http
                    .cors().disable()
                    .csrf().disable()
                    .build();
        }
        log.warn("Spring Security will ignore following URLs: {}",
                 Lists.newArrayList(this.mafConfiguration.flattenIgnoredUrls()));
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
                .securityContextRepository(serverSecurityContextRepository)
                .authenticationManager(reactiveAuthenticationManager)
                .authorizeExchange()
                .pathMatchers(this.mafConfiguration.flattenIgnoredUrls()).permitAll()
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
    public ServerSecurityContextRepository serverSecurityContextRepository(ReactiveAuthenticationManager reactiveAuthenticationManager) {
        return new JwtReactiveServerSecurityContextRepositoryImpl(this.mafConfiguration, reactiveAuthenticationManager,
                                                                  this.authCenterRemoteApi);
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new JwtReactiveAuthenticationManagerImpl(this.authCenterRemoteApi);
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager() {
        return new RbacReactiveAuthorizationManagerImpl(this.authCenterRemoteApi);
    }
}
