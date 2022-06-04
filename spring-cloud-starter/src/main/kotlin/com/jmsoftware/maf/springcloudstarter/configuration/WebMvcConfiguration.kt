package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.DateTimeFormatterFactoryBean
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.context.request.async.CallableProcessingInterceptor
import org.springframework.web.cors.CorsConfiguration.ALL
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


/**
 * # WebMvcConfiguration
 *
 * Spring MVC Configurations.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 11:02 AM
 */
class WebMvcConfiguration(
    private val threadPoolTaskExecutor: ThreadPoolTaskExecutor,
    private val callableProcessingInterceptor: CallableProcessingInterceptor
) : WebMvcConfigurer {
    companion object {
        /**
         * Max age: 3600 seconds (1 hour). Configure how long in seconds the response from a pre-flight request
         * can be cached by clients. By default, this is set to 1800 seconds (30 minutes).
         */
        private const val MAX_AGE_SECS: Long = 3600
        private val log = logger()
    }

    /**
     * Configure cross origin requests processing.
     *
     * @param registry CORS registry
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        log.info("Configuring CORS allowedOrigins: $ALL, allowedMethods: $ALL, allowedHeaders: $ALL")
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedOriginPatterns(ALL)
            .allowedMethods(ALL)
            .allowedHeaders(ALL)
            .maxAge(MAX_AGE_SECS)
    }

    override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
        configurer.setDefaultTimeout(360000)
            .setTaskExecutor(threadPoolTaskExecutor)
            .registerCallableInterceptors(callableProcessingInterceptor)
    }

    override fun addFormatters(registry: FormatterRegistry) {
        DateTimeFormatterRegistrar().apply {
            this.setUseIsoFormat(true)
            this.registerFormatters(registry)
        }
    }
}
