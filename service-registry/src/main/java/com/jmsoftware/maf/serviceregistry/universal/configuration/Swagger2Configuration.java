package com.jmsoftware.maf.serviceregistry.universal.configuration;

import lombok.RequiredArgsConstructor;
import lombok.val;
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

/**
 * <h1>Swagger2Configuration</h1>
 * <p>
 * Swagger 2 Configuration
 * <a href="http://192.168.1.4:8080/jm-spring-boot-template-dev/swagger-ui.html">Click me to view<a/>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-02-07 16:15
 **/
@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class Swagger2Configuration {
    private final ProjectProperty projectProperty;
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(projectProperty.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        val projectArtifactId = projectProperty.getProjectArtifactId();
        val version = projectProperty.getVersion();
        val developerEmail = projectProperty.getDeveloperEmail();
        val developerUrl = projectProperty.getDeveloperUrl();
        return new ApiInfoBuilder()
                .title(String.format("API for %s@%s", projectArtifactId, version))
                .description(String.format("%s %sArtifact ID: %s%sEnvironment: %s",
                                           projectProperty.getDescription(),
                                           LINE_SEPARATOR,
                                           projectArtifactId,
                                           LINE_SEPARATOR,
                                           projectProperty.getEnvironment()))
                .contact(new Contact(String.format("%s, email: %s%sHome page: %s",
                                                   projectProperty.getDeveloperName(),
                                                   developerEmail,
                                                   LINE_SEPARATOR,
                                                   developerUrl),
                                     developerUrl, developerEmail))
                .version(version)
                .build();
    }
}
