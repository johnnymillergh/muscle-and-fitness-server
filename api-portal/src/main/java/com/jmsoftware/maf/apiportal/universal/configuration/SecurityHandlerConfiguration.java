package com.jmsoftware.maf.apiportal.universal.configuration;

import com.jmsoftware.maf.common.constant.HttpStatus;
import com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
            val formattedMessage = String.format("Authentication encountered an exception! Exception message: %s",
                                                 authException.getMessage());
            log.error(formattedMessage);
            ResponseUtil.renderJson(response, HttpStatus.FORBIDDEN, formattedMessage);
        });
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> {
            val formattedMessage = String.format("Access was denied! Exception message: %s",
                                                 accessDeniedException.getMessage());
            log.error(formattedMessage);
            ResponseUtil.renderJson(response, HttpStatus.FORBIDDEN, formattedMessage);
        });
    }
}
