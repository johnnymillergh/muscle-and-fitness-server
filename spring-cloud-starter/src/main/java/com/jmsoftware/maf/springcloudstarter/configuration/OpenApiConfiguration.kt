package com.jmsoftware.maf.springcloudstarter.configuration

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.context.annotation.Bean
import java.util.function.Consumer

/**
 * # OpenApiConfiguration
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 10:41 AM
 */
class OpenApiConfiguration(
    private val mafProjectProperties: MafProjectProperties
) {
    companion object {
        private const val DEV = "development"
        private val log = logger()
    }

    @Bean
    fun openApi(): OpenAPI {
        val projectArtifactId = mafProjectProperties.projectArtifactId
        val version = mafProjectProperties.version
        val developerEmail = mafProjectProperties.developerEmail
        val developerUrl = mafProjectProperties.developerUrl
        return OpenAPI()
            .info(
                Info()
                    .title("API for $projectArtifactId@$version")
                    .description("${mafProjectProperties.description}, artifact ID: $projectArtifactId, environment: ${mafProjectProperties.environment}")
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

    @Bean
    @Suppress("SpellCheckingInspection")
    fun openApiCustomiser(): OpenApiCustomiser {
        return OpenApiCustomiser { openApi: OpenAPI ->
            openApi.servers.forEach(Consumer { server: Server ->
                if (!StrUtil.containsIgnoreCase(
                        mafProjectProperties.environment, DEV
                    )
                ) {
                    server.url = "${server.url}/${mafProjectProperties.projectArtifactId}"
                }
                server.description = "Modified server URL - ${mafProjectProperties.projectArtifactId}"
                log.info("Modified server, $server")
            })
        }
    }
}
