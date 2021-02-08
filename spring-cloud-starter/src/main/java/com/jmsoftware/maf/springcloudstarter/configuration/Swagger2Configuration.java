package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

/**
 * <h1>Swagger2Configuration</h1>
 * <p>
 * Swagger 2 Configuration
 * <a href="http://192.168.1.4:8080/jm-spring-boot-template-dev/swagger-ui.html">Click me to view<a/>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-02-07 16:15
 **/
@Slf4j
@Configuration
@EnableSwagger2
@RequiredArgsConstructor
@ConditionalOnProperty(value = "maf.configuration.swagger-disabled", havingValue = "false")
public class Swagger2Configuration {
    private final MafProjectProperty mafProjectProperty;

    @PostConstruct
    private void postConstruct() {
        log.warn("[UNSAFE] Swagger 2 is enabled, the internal and external APIs will be exposed");
    }

    public ApiInfo apiInfo() {
        val projectArtifactId = mafProjectProperty.getProjectArtifactId();
        val version = mafProjectProperty.getVersion();
        val developerEmail = mafProjectProperty.getDeveloperEmail();
        val developerUrl = mafProjectProperty.getDeveloperUrl();
        return new ApiInfoBuilder()
                .title(String.format("API for %s@%s", projectArtifactId, version))
                .description(String.format("%s Artifact ID: %s Environment: %s",
                                           mafProjectProperty.getDescription(),
                                           projectArtifactId,
                                           mafProjectProperty.getEnvironment()))
                .contact(new Contact(String.format("%s, email: %s Home page: %s",
                                                   mafProjectProperty.getDeveloperName(),
                                                   developerEmail,
                                                   developerUrl),
                                     developerUrl, developerEmail))
                .version(version)
                .build();
    }

    @Bean
    public Docket docket(Swagger2Configuration swagger2Configuration, MafProjectProperty mafProjectProperty) {
        log.warn("Initial bean: '{}'", Docket.class.getSimpleName());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swagger2Configuration.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(mafProjectProperty.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }
}