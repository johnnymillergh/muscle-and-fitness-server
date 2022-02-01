package com.jmsoftware.maf.apigateway.universal.configuration;

import cn.hutool.core.collection.CollUtil;
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafProjectProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <h1>OpenApiConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/1/22 11:51 PM
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {
    private static final String SWAGGER_API_URI = "/v3/api-docs";
    private final SwaggerConfiguration swaggerConfiguration;
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;
    private final MafProjectProperties mafProjectProperties;
    private final DiscoveryClient discoveryClient;

    @Bean
    public OpenAPI openApi() {
        val projectArtifactId = this.mafProjectProperties.getProjectArtifactId();
        val version = this.mafProjectProperties.getVersion();
        val developerEmail = this.mafProjectProperties.getDeveloperEmail();
        val developerUrl = this.mafProjectProperties.getDeveloperUrl();
        return new OpenAPI()
                .info(
                        new Info()
                                .title(String.format("API for %s@%s", projectArtifactId, version))
                                .description(
                                        String.format("%s, artifact ID: %s, environment: %s",
                                                      this.mafProjectProperties.getDescription(),
                                                      projectArtifactId,
                                                      this.mafProjectProperties.getEnvironment())
                                )
                                .contact(
                                        new Contact()
                                                .name(this.mafProjectProperties.getDeveloperName())
                                                .email(developerEmail)
                                                .url(developerUrl)
                                )
                                .version(version)
                                .license(new License().name("LinkedIn").url(developerUrl))
                );
    }

    @Bean
    public List<GroupedOpenApi> groupedOpenApiList() {
        val services = CollUtil.newArrayList(this.discoveryClient.getServices());
        services.add(this.mafProjectProperties.getProjectArtifactId());
        val groups = CollUtil.<GroupedOpenApi>newArrayList();
        this.swaggerUiConfigProperties.setUrls(CollUtil.newHashSet());
        services.forEach(serviceName -> {
            if (!CollUtil.contains(this.swaggerConfiguration.getIgnoredServiceIds(), serviceName)) {
                log.warn("Found discovery client. Service name: {}", serviceName);
                groups.add(GroupedOpenApi.builder().pathsToMatch("/" + serviceName + "/**").group(serviceName).build());
                val swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
                swaggerUrl.setUrl(serviceName + "/" + SWAGGER_API_URI);
                swaggerUrl.setName(serviceName);
                this.swaggerUiConfigProperties.getUrls().add(swaggerUrl);
            }
        });
        return groups;
    }
}
