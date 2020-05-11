package com.jmsoftware.apiportal.universal.configuration;

import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * <h1>SecurityHandlerConfiguration</h1>
 * <p>
 * Security handler configuration.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Slf4j
@Configuration
public class SecurityHandlerConfiguration {
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return ((request, response, authException) -> {
            log.error("Authentication encountered an exception! Exception message: {}", authException.getMessage(),
                      authException);
            ResponseUtil.renderJson(response, HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getMessage());
        });
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> {
            log.error("Access was denied! Exception message: {}", accessDeniedException.getMessage(),
                      accessDeniedException);
            ResponseUtil.renderJson(response, HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getMessage());
        });
    }
}
