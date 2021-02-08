package com.jmsoftware.maf.authcenter.permission.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * Description: PermissionConfiguration, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 2/8/2021 5:18 PM
 **/
@Data
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "permission")
public class PermissionConfiguration {
    @NotEmpty
    private Set<@NotBlank String> ignoredServiceIds;
}
