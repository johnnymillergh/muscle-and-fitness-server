package com.jmsoftware.maf.authcenter.permission.configuration

import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.validation.annotation.Validated

/**
 * Description: PermissionConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 5:35 PM
 */
@Validated
@RefreshScope
//@Configuration
@ConfigurationProperties(prefix = PermissionConfiguration.PREFIX)
class PermissionConfiguration(
    @NotEmpty val ignoredServiceIds: Set<String>
) {
    companion object {
        const val PREFIX = "maf.configuration.permission"
    }
}
