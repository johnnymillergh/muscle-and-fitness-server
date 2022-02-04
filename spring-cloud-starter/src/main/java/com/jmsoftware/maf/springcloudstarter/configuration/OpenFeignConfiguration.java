/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your
 * system.
 */
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
