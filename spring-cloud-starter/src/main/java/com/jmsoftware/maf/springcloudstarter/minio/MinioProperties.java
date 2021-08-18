package com.jmsoftware.maf.springcloudstarter.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>MinioProperty</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/7/21 9:39 PM
 */
@Data
@Validated
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
public class MinioProperties {
    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "minio";
    /**
     * The Enabled.
     */
    @NotNull
    private Boolean enabled = Boolean.FALSE;
    /**
     * The Endpoint.
     */
    @NotBlank
    private String endpoint;
    /**
     * The Port.
     */
    @NotNull
    private Integer port;
    /**
     * The Access key. User ID
     */
    @NotBlank
    private String accessKey;
    /**
     * The Secret key. Password
     */
    @NotBlank
    private String secretKey;
    /**
     * The Secure.
     */
    private Boolean secure = Boolean.FALSE;
    /**
     * The Bucket name.
     */
    private String bucketName;
    /**
     * The Config dir.
     */
    private String configDir;
}
