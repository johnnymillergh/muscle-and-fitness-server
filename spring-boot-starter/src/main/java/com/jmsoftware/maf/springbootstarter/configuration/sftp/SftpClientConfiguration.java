package com.jmsoftware.maf.springbootstarter.configuration.sftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * <h1>SftpClientConfiguration</h1>
 * <p>SFTP client configuration</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-07-04 18:18
 **/
@Data
@Component
@ConfigurationProperties(prefix = "sftp.client.configuration")
public class SftpClientConfiguration {
    /**
     * SFTP server IP
     */
    private String host;
    /**
     * SFTP server port
     */
    private Integer port;
    /**
     * Login user
     */
    private String user;
    /**
     * Login password
     */
    private String password;
    /**
     * Remote directory
     */
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
