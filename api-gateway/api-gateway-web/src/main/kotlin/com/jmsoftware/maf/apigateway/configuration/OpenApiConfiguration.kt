package com.jmsoftware.maf.apigateway.configuration

import cn.hutool.core.collection.CollUtil
import com.jmsoftware.maf.apigateway.property.SwaggerConfigurationProperties
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafProjectProperties
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.AbstractSwaggerUiConfigProperties.SwaggerUrl
import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.SwaggerUiConfigProperties
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * # OpenApiConfiguration
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:18 PM
 */
@Configuration
class OpenApiConfiguration(
    private val swaggerConfigurationProperties: SwaggerConfigurationProperties,
    private val swaggerUiConfigProperties: SwaggerUiConfigProperties,
    private val mafProjectProperties: MafProjectProperties,
    private val discoveryClient: DiscoveryClient
) {
    companion object {
        const val GROUPED_OPEN_API_LIST_NAME = "groupedOpenApiList"
        private const val SWAGGER_API_URI = "/v3/api-docs"
        private val log = logger()
    }

    @Bean
    fun openApi(): OpenAPI {
        val projectArtifactId = mafProjectProperties.projectParentArtifactId
        val version = mafProjectProperties.version
        val developerEmail = mafProjectProperties.developerEmail
        val developerUrl = mafProjectProperties.developerUrl
        return OpenAPI()
            .info(
                Info()
                    .title("API for $projectArtifactId@$version")
                    .description("$mafProjectProperties.description, artifact ID: $projectArtifactId, environment: ${mafProjectProperties.environment}")
                    .contact(
                        Contact()
                            .name(mafProjectProperties.developerName)
                            .email(developerEmail)
                            .url(developerUrl)
                    )
                    .version(version)
                    .license(License().name("LinkedIn").url(developerUrl))
            )
    }

    @Bean(GROUPED_OPEN_API_LIST_NAME)
    fun groupedOpenApiList(): List<GroupedOpenApi> {
        return createGroupedOpenApiList()
    }

    fun createGroupedOpenApiList(): List<GroupedOpenApi> {
        val groups = mutableListOf<GroupedOpenApi>()
        swaggerUiConfigProperties.urls = mutableSetOf()
        mutableListOf<String>().apply {
            this.addAll(discoveryClient.services)
            this.add(mafProjectProperties.projectArtifactId.removeSuffix("-bootstrap"))
        }.forEach { serviceName: String ->
            if (!CollUtil.contains(swaggerConfigurationProperties.ignoredServiceIds, serviceName)) {
                log.warn("Found discovery client. Service name: $serviceName")
                groups.add(GroupedOpenApi.builder().pathsToMatch("/$serviceName/**").group(serviceName).build())
                val swaggerUrl = SwaggerUrl()
                swaggerUrl.url = serviceName + SWAGGER_API_URI
                swaggerUrl.name = serviceName
                swaggerUiConfigProperties.urls.add(swaggerUrl)
            }
        }
        return groups
    }
}
