package com.jmsoftware.maf.springcloudstarter.configuration

import com.jmsoftware.maf.common.util.logger
import lombok.extern.slf4j.Slf4j
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

/**
 * # RestTemplateConfiguration
 *
 * Description: RestTemplateConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:46 AM
 */
@Slf4j
class RestTemplateConfiguration {
    companion object {
        private val log = logger()
    }

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate {
        val poolingHttpClientConnectionManager = PoolingHttpClientConnectionManager()
        poolingHttpClientConnectionManager.maxTotal = 1000
        poolingHttpClientConnectionManager.defaultMaxPerRoute = 1000
        val httpClientBuilder = HttpClients.custom()
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager)
        val httpClient = httpClientBuilder.build()
        val httpComponentsClientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(httpClient)
        httpComponentsClientHttpRequestFactory.setConnectTimeout(6000)
        httpComponentsClientHttpRequestFactory.setReadTimeout(6000)
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(200)
        val restTemplate = RestTemplate()
        restTemplate.requestFactory = httpComponentsClientHttpRequestFactory
        restTemplate.errorHandler = DefaultResponseErrorHandler()
        log.warn("Initial bean: '${restTemplate.javaClass.simpleName}'")
        return restTemplate
    }
}
