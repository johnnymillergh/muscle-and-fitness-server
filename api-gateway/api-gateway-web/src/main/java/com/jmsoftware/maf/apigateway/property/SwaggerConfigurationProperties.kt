package com.jmsoftware.maf.apigateway.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * <h1>SwaggerConfigurationProperties</h1>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/7/2021 3:28 PM
 **/
@Data
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = SwaggerConfigurationProperties.PREFIX)
public class SwaggerConfigurationProperties {
    public static final String PREFIX = "maf.configuration.swagger";
    /**
     * Ignored service id set
     */
   @NotEmpty
   private Set<@NotBlank String> ignoredServiceIds;
}
