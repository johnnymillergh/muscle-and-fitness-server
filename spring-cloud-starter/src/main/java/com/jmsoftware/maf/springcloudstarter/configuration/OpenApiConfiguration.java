package com.jmsoftware.maf.springcloudstarter.configuration;

import cn.hutool.core.text.CharSequenceUtil;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * <h1>OpenApiConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com, 2/2/22 2:25 AM
 **/
@Slf4j
@RequiredArgsConstructor
public class OpenApiConfiguration {
    private static final String DEV = "development";
    private final MafProjectProperties mafProjectProperties;

    @Bean
    public OpenAPI openApi() {
        val projectArtifactId = this.mafProjectProperties.getProjectArtifactId();
        val version = this.mafProjectProperties.getVersion();
        val developerEmail = this.mafProjectProperties.getDeveloperEmail();
        val developerUrl = this.mafProjectProperties.getDeveloperUrl();
        return new OpenAPI()
                .info(
                        new Info()
                                .title(format("API for {}@{}", projectArtifactId, version))
                                .description(
                                        format("{}, artifact ID: {}, environment: {}",
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
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> openApi.getServers().forEach(server -> {
            if (!CharSequenceUtil.containsIgnoreCase(this.mafProjectProperties.getEnvironment(), DEV)) {
                server.setUrl(format("{}/{}", server.getUrl(), this.mafProjectProperties.getProjectArtifactId()));
            }
            server.setDescription(format("Modified server URL - {}", this.mafProjectProperties.getProjectArtifactId()));
            log.info("Modified server, {}", server);
        });
    }
}
