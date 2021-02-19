package com.jmsoftware.maf.springcloudstarter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * <h1>WebMvcConfiguration</h1>
 * <p>
 * Spring MVC Configurations.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 1/23/20 9:02 AM
 **/
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    /**
     * Max age: 3600 seconds (1 hour)
     */
    private static final long MAX_AGE_SECS = 3600;
    /**
     * Default name of the locale specification parameter: "lang".
     */
    private static final String DEFAULT_PARAM_NAME = "lang";

    /**
     * Configure cross origin requests processing.
     *
     * @param registry CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }

    /**
     * An interceptor bean that will switch to a new locale based on the value of the lang parameter appended to a
     * request.
     *
     * @return the locale change interceptor
     * @see
     * <a href='https://www.baeldung.com/spring-boot-internationalization#localechangeinterceptor'>LocaleChangeInterceptor</a>
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(DEFAULT_PARAM_NAME);
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // In order to take effect, this bean needs to be added to the application's interceptor registry.
        registry.addInterceptor(localeChangeInterceptor());
    }
}
