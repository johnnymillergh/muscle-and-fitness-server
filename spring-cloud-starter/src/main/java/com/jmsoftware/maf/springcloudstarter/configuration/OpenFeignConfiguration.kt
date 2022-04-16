package com.jmsoftware.maf.springcloudstarter.configuration

import feign.Target
import org.springframework.cloud.openfeign.CircuitBreakerNameResolver
import org.springframework.context.annotation.Bean
import java.lang.reflect.Method

/**
 * # OpenFeignConfiguration
 *
 * Description: OpenFeignConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:45 AM
 * @see <a href='https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/.spring-cloud-feign-circuitbreaker'>Feign Spring Cloud CircuitBreaker Support</a>
 */
class OpenFeignConfiguration {
    @Bean
    fun circuitBreakerNameResolver(): CircuitBreakerNameResolver {
        return CircuitBreakerNameResolver { feignClientName: String, _: Target<*>, method: Method -> feignClientName + "_" + method.name }
    }
}
