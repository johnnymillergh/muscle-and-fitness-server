package com.jmsoftware.maf.gateway.universal.configuration;

import com.jmsoftware.maf.gateway.universal.filter.RequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

import java.util.ArrayList;

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
    private final CustomConfiguration customConfiguration;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;
    private final ServerAuthenticationEntryPointImpl serverAuthenticationEntryPointImpl;
    private final CustomServerAccessDeniedHandler customServerAccessDeniedHandler;
    private final RequestFilter requestFilter;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(serverAuthenticationEntryPointImpl)
                .accessDeniedHandler(customServerAccessDeniedHandler)
                .and()
                .addFilterBefore(requestFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(flattenIgnoredUrls()).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated()
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private String[] flattenIgnoredUrls() {
        final var ignoredRequests = customConfiguration.getIgnoredRequest();
        final var flattenIgnoredUrls = new ArrayList<String>();
        flattenIgnoredUrls.addAll(ignoredRequests.getGet());
        flattenIgnoredUrls.addAll(ignoredRequests.getPost());
        flattenIgnoredUrls.addAll(ignoredRequests.getDelete());
        flattenIgnoredUrls.addAll(ignoredRequests.getPut());
        flattenIgnoredUrls.addAll(ignoredRequests.getHead());
        flattenIgnoredUrls.addAll(ignoredRequests.getPatch());
        flattenIgnoredUrls.addAll(ignoredRequests.getOptions());
        flattenIgnoredUrls.addAll(ignoredRequests.getTrace());
        flattenIgnoredUrls.addAll(ignoredRequests.getPattern());
        log.info("Ignored URL list for WebFlux security: {}", flattenIgnoredUrls);
        return flattenIgnoredUrls.toArray(new String[0]);
    }
}
