package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>MinioConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 4/29/21 2:23 PM
 **/
@Data
@Slf4j
@Validated
@Component
@ConfigurationProperties(prefix = MinioClientConfiguration.PREFIX)
public class MinioClientConfiguration {
    public static final String PREFIX = "minio.client.configuration";
    @NotBlank
    private String endpoint;
    @NotNull
    private Integer port;
    @NotBlank
    private String accessKey;
    @NotBlank
    private String secretKey;
    private Boolean secure = Boolean.TRUE;
    private String bucketName;
    private String configDir;
}
