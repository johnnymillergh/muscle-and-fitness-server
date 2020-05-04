package com.jmsoftware.authcenter.universal.configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <h1>JwtConfiguration</h1>
 * <p>
 * JWT configuration
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/12/20 3:03 PM
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt.configuration")
public class JwtConfiguration {
    public JwtConfiguration(ProjectProperty projectProperty) {
        this.signingKey = projectProperty.getProjectArtifactId();
        log.error("JWT signing key: {}", this.signingKey);
    }

    /**
     * JWT signing key, which is equal to the string value of group id of project.
     */
    private String signingKey;
    /**
     * Time to live of JWT. Default: 3 * 600000 milliseconds (30 min).
     */
    private Long ttl = 3 * 600000L;
    /**
     * Time to live of JWT for remember me, Default: 7 * 86400000 milliseconds (7 day)
     */
    private Long ttlForRememberMe = 7 * 86400000L;
}