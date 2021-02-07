package com.jmsoftware.maf.apigateway.universal.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * <h1>SwaggerConfiguration</h1>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/7/2021 3:28 PM
 **/
@Data
@Validated
@Component
@ConfigurationProperties(prefix = "maf.configuration.swagger")
public class SwaggerConfiguration {
    /**
     * Ignored service id set
     */
    private Set<@NotBlank String> ignoredServiceIdSet;
}
