package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * <h1>WebMvcConfiguration</h1>
 * <p>
 * Spring MVC Configurations.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 1/23/20 9:02 AM
 **/
@Slf4j
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    /**
     * Max age: 3600 seconds (1 hour). Configure how long in seconds the response from a pre-flight request
     * can be cached by clients. By default this is set to 1800 seconds (30 minutes).
     */
    private static final long MAX_AGE_SECS = 3600;

    /**
     * Configure cross origin requests processing.
     *
     * @param registry CORS registry
     */
    @SuppressWarnings("BroadCORSAllowOrigin")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Configuring CORS allowedOrigins: {}, allowedMethods: {}, allowedHeaders: {}", ALL, ALL, ALL);
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns(ALL)
                .allowedMethods(ALL)
                .allowedHeaders(ALL)
                .maxAge(MAX_AGE_SECS);
    }
}
