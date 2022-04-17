package com.jmsoftware.maf.apigateway.configuration

import cn.hutool.core.util.BooleanUtil
import com.jmsoftware.maf.apigateway.remote.AuthCenterWebClientService
import com.jmsoftware.maf.apigateway.security.impl.*
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.reactivespringcloudstarter.util.ResponseUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.context.ServerSecurityContextRepository

/**
 * # WebFluxSecurityConfiguration
 *
 * Description: WebFluxSecurityConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 7:52 AM
 * @see <a href='https://docs.spring.io/spring-security/reference/servlet/configuration/kotlin.html#_multiple_httpsecurity'>Kotlin Configuration</a>
 * @see
 * <a href='https://docs.spring.io/spring-security/site/docs/5.3.6.RELEASE/reference/html5/#reactive-applications'>Spring Secirity Reference - Reactive Applications</a>
 * @see
 * <a href='https://www.devglan.com/spring-security/spring-security-webflux-jwt'>Securing Spring WebFlux Reactive APIs with JWT Auth</a>
 * @see
 * <a href='https://blog.csdn.net/tiancao222/article/details/104375924'>SpringCloud Gateway 整合 Spring Security Webflux 的关键点（痛点解析），及示例项目</a>
 */
@Configuration
@EnableWebFluxSecurity
class WebFluxSecurityConfiguration(
    private val mafConfigurationProperties: MafConfigurationProperties,
    private val authCenterWebClientService: AuthCenterWebClientService
) {
    companion object {
        private val log = logger()
    }

    @Bean
    fun springWebFilterChain(
        http: ServerHttpSecurity,
        serverAuthenticationEntryPoint: ServerAuthenticationEntryPoint,
        serverAccessDeniedHandler: ServerAccessDeniedHandler,
        serverSecurityContextRepository: ServerSecurityContextRepository,
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
        reactiveAuthorizationManager: ReactiveAuthorizationManager<AuthorizationContext>
    ): SecurityWebFilterChain {
        if (BooleanUtil.isFalse(mafConfigurationProperties.webSecurityEnabled)) {
            log.warn("Web security was disabled.")
            return http
                .cors().disable()
                .csrf().disable()
                .build()
        }
        val ignoredUrls = mafConfigurationProperties.flattenIgnoredUrls().toTypedArray()
        log.warn("Spring Security will ignore following URLs: $ignoredUrls")
        return http
            .cors().disable()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling()
            .authenticationEntryPoint(serverAuthenticationEntryPoint)
            .accessDeniedHandler(serverAccessDeniedHandler)
            .and() // Authentication
            .securityContextRepository(serverSecurityContextRepository)
            .authenticationManager(reactiveAuthenticationManager)
            .authorizeExchange()
            .pathMatchers(*ignoredUrls).permitAll()
            .pathMatchers(HttpMethod.OPTIONS).permitAll() // Authorization
            .anyExchange().access(reactiveAuthorizationManager)
            .and()
            .build()
    }

    @Bean
    fun serverAuthenticationEntryPoint(responseUtil: ResponseUtil): ServerAuthenticationEntryPoint {
        return ServerAuthenticationEntryPointImpl(responseUtil)
    }

    @Bean
    fun serverAccessDeniedHandler(responseUtil: ResponseUtil): ServerAccessDeniedHandler {
        return GatewayServerAccessDeniedHandlerImpl(responseUtil)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun serverSecurityContextRepository(reactiveAuthenticationManager: ReactiveAuthenticationManager): ServerSecurityContextRepository {
        return JwtReactiveServerSecurityContextRepositoryImpl(
            mafConfigurationProperties,
            reactiveAuthenticationManager,
            authCenterWebClientService
        )
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        return JwtReactiveAuthenticationManagerImpl(authCenterWebClientService)
    }

    @Bean
    fun reactiveAuthorizationManager(): ReactiveAuthorizationManager<AuthorizationContext> {
        return RbacReactiveAuthorizationManagerImpl(authCenterWebClientService)
    }
}
