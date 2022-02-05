package com.jmsoftware.maf.springcloudstarter.configuration;

import feign.Target;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.CircuitBreakerNameResolver;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;

/**
 * Description: OpenFeignConfiguration, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/5/2022 7:15 PM
 * @see
 * <a href='https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign-circuitbreaker'>Feign Spring Cloud CircuitBreaker Support</a>
 **/
@Slf4j
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OpenFeignConfiguration {
    @Bean
    public CircuitBreakerNameResolver circuitBreakerNameResolver() {
        return (String feignClientName, Target<?> target, Method method) -> feignClientName + "_" + method.getName();
    }
}
