package com.jmsoftware.maf.apiportal.universal.configuration;

import com.jmsoftware.maf.apiportal.universal.filter.JwtAuthenticationFilter;
import com.jmsoftware.maf.apiportal.universal.service.impl.CustomUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Optional;

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
    private final CustomConfiguration customConfiguration;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsServiceImpl).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Check if disable Web Security.
        if (customConfiguration.getWebSecurityDisabled()) {
            http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
            return;
        }

        http.cors()
                // Disable CSRF (Cross-site request forgery)
                .and().csrf().disable()
                // Disable form login to use custom login
                .formLogin().disable()
                .httpBasic().disable()

                // Allows restricting access based upon the HttpServletRequest
                .authorizeRequests()
                // RBAC URL authorization
                .anyRequest()
                .access("@rbacAuthorityServiceImpl.hasPermission(request,authentication)")

                // Disable logout to use custom logout
                .and().logout().disable()
                .sessionManagement()
                // Disable session management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // Exception handling
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);

        // Add customized JWT filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Add ignored HTTP request list
     * Same as {@link #configure(HttpSecurity)}
     * {@code http.authorizeRequests().antMatchers("/api/auth/**").permitAll()}
     */
    @Override
    public void configure(WebSecurity web) {
        val and = web.ignoring().and();
        Optional.ofNullable(customConfiguration.getIgnoredRequest())
                .ifPresentOrElse((ignoredRequest -> {
                    ignoredRequest.getGet().forEach(url -> and.ignoring().antMatchers(HttpMethod.GET, url));
                    ignoredRequest.getPost().forEach(url -> and.ignoring().antMatchers(HttpMethod.POST, url));
                    ignoredRequest.getDelete().forEach(url -> and.ignoring().antMatchers(HttpMethod.DELETE, url));
                    ignoredRequest.getPut().forEach(url -> and.ignoring().antMatchers(HttpMethod.PUT, url));
                    ignoredRequest.getHead().forEach(url -> and.ignoring().antMatchers(HttpMethod.HEAD, url));
                    ignoredRequest.getPatch().forEach(url -> and.ignoring().antMatchers(HttpMethod.PATCH, url));
                    ignoredRequest.getOptions().forEach(url -> and.ignoring().antMatchers(HttpMethod.OPTIONS, url));
                    ignoredRequest.getTrace().forEach(url -> and.ignoring().antMatchers(HttpMethod.TRACE, url));
                    ignoredRequest.getPattern().forEach(url -> and.ignoring().antMatchers(url));
                }), () -> log.warn("Security warning: Ignored request is empty! The ignored request configuration " +
                                   "might be invalid!"));
    }
}
