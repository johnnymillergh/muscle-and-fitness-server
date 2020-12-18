package com.jmsoftware.maf.gateway.universal.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * <h1>WebFluxConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/17/20 5:16 PM
 **/
@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {
    /**
     * Add resource handlers for serving static resources.
     *
     * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
     * @date 2/17/20 5:19 PM
     * @see ResourceHandlerRegistry registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
