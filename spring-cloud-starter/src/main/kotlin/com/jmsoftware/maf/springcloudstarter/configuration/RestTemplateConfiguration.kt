package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

/**
 * # RestTemplateConfiguration
 *
 * Description: RestTemplateConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:46 AM
 */
class RestTemplateConfiguration {
    companion object {
        private val log = logger()
    }

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate {
        val requestFactory = OkHttp3ClientHttpRequestFactory()
        requestFactory.setConnectTimeout(60 * 1000)
        requestFactory.setReadTimeout(60 * 1000)
        val restTemplate = RestTemplate()
        restTemplate.requestFactory = requestFactory
        restTemplate.errorHandler = DefaultResponseErrorHandler()
        log.warn("Initial bean: '${restTemplate.javaClass.simpleName}'")
        return restTemplate
    }
}
