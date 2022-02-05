package com.jmsoftware.maf.springcloudstarter.configuration;

import feign.Target;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.CircuitBreakerNameResolver;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;

/**
 * Description: OpenFeignConfiguration, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/3/2022 3:52 PM
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
