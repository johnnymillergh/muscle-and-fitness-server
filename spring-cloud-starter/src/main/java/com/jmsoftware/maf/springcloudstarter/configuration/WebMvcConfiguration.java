package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h1>WebMvcConfiguration</h1>
 * <p>
 * Spring MVC Configurations.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 1/23/20 9:02 AM
 **/
@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    /**
     * Max age: 3600 seconds (1 hour)
     */
    private static final long MAX_AGE_SECS = 3600;

    /**
     * Configure cross origin requests processing.
     *
     * @param registry CORS registry
     */
    @Override
    @SuppressWarnings("BroadCORSAllowOrigin")
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
}
