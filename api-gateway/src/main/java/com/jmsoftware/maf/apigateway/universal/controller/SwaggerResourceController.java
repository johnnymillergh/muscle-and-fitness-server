package com.jmsoftware.maf.apigateway.universal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.*;


/**
 * <h1>SwaggerResourceController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/15/20 6:07 PM
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/swagger-resources")
public class SwaggerResourceController {
    private final SwaggerResourcesProvider swaggerResources;
    private final ApplicationContext applicationContext;

    @GetMapping("/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
        SecurityConfiguration securityConfiguration;
        try {
            securityConfiguration = applicationContext.getBean(SecurityConfiguration.class);
            log.info("Bean [{}] is found, {}", SecurityConfiguration.class.getSimpleName(), securityConfiguration);
        } catch (BeansException e) {
            securityConfiguration = SecurityConfigurationBuilder.builder().build();
            log.warn("Bean [{}] is null, create one: {}", SecurityConfiguration.class.getSimpleName(),
                     securityConfiguration);
        }
        return Mono.just(new ResponseEntity<>(securityConfiguration, HttpStatus.OK));
    }

    @GetMapping("/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
        UiConfiguration uiConfiguration;
        try {
            uiConfiguration = applicationContext.getBean(UiConfiguration.class);
            log.info("Bean [{}] is found, {}", UiConfiguration.class.getSimpleName(), uiConfiguration);
        } catch (BeansException e) {
            uiConfiguration = UiConfigurationBuilder.builder().build();
            log.warn("Bean [{}] is null, create one: {}", UiConfiguration.class.getSimpleName(), uiConfiguration);
        }
        return Mono.just(new ResponseEntity<>(uiConfiguration, HttpStatus.OK));
    }

    @GetMapping("")
    public Mono<ResponseEntity<?>> swaggerResources() {
        return Mono.just((new ResponseEntity<>(swaggerResources.get(), HttpStatus.OK)));
    }
}
