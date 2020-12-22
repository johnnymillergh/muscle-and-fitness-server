package com.jmsoftware.maf.apigateway.universal.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.LinkedList;
import java.util.List;

/**
 * <h1>SwaggerResourceProvider</h1>
 * <p>
 * Change description here.
 * <p>
 * https://blog.csdn.net/ttzommed/article/details/81103609
 * <p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/15/20 5:40 PM
 * @see
 * <a href='https://doc.xiaominfo.com/guide/ui-front-gateway.html#%E6%96%87%E6%A1%A3%E8%81%9A%E5%90%88%E4%B8%9A%E5%8A%A1%E7%BC%96%E7%A0%81'>文档聚合业务编码</a>
 **/
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class SwaggerResourceProvider implements SwaggerResourcesProvider {
    private static final String SWAGGER_API_URI = "/v2/api-docs";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final ProjectProperty projectProperty;
    private final RouteLocator routeLocator;

    /**
     * Generate Swagger resource.
     *
     * @return Swagger resource.
     * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
     * @date 2/15/20 11:06 PM
     */
    @Override
    public List<SwaggerResource> get() {
        val swaggerResourceList = new LinkedList<SwaggerResource>();
        routeLocator.getRoutes().subscribe(route -> {
            val serviceName = route.getUri().toString().substring(5).toLowerCase();
            log.warn("{} found dynamic route. Service name: {}, route: {}", this.getClass().getSimpleName(),
                     serviceName, route);
            val swaggerResource = new SwaggerResource();
            swaggerResource.setName(serviceName.toUpperCase());
            swaggerResource.setLocation(String.format("%s%s", serviceName, SWAGGER_API_URI));
            swaggerResource.setSwaggerVersion("2.0");
            log.warn("Exposed Swagger Resource: {}", swaggerResource.toString());
            swaggerResourceList.add(swaggerResource);
        });
        return swaggerResourceList;
    }

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
