package com.jmsoftware.maf.springcloudstarter.sftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>SftpClientConfiguration</h1>
 * <p>SFTP client configuration</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-07-04 18:18
 **/
@Data
@Component
@Validated
@ConfigurationProperties(prefix = SftpClientConfiguration.PREFIX)
public class SftpClientConfiguration {
    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "sftp";
    /**
     * The Enabled.
     */
    @NotNull
    private Boolean enabled = Boolean.FALSE;
    /**
     * SFTP server IP
     */
    @NotBlank
    private String host;
    /**
     * SFTP server port
     */
    @NotNull
    private Integer port;
    /**
     * Login user
     */
    @NotBlank
    private String user;
    /**
     * Login password
     */
    @NotBlank
    private String password;
    /**
     * Remote directory
     */
    @NotBlank
    private String directory;
    /**
     * Private key
     */
    private Resource privateKey;
    /**
     * Private key pass phrase
     */
    private String privateKeyPassPhrase;
    /**
     * The maximum cache size of session. Default: 10
     */
    private Integer sessionCacheSize = 10;
    /**
     * The session wait timeout (time unit: MILLISECONDS). Default: 10 * 1000L (10 seconds)
     */
    private Long sessionWaitTimeout = 10 * 1000L;
}
