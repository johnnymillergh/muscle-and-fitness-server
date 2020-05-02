package com.jmsoftware.apiportal.universal.configuration;

import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.util.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * <h1>SecurityHandlerConfiguration</h1>
 * <p>
 * Security handler configuration.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Configuration
public class SecurityHandlerConfiguration {
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> ResponseUtil.renderJson(response,
                                                                                     HttpStatus.FORBIDDEN,
                                                                                     null);
    }
}
