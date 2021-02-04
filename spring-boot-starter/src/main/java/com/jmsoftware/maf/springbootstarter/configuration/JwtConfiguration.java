package com.jmsoftware.maf.springbootstarter.configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;

/**
 * <h1>JwtConfiguration</h1>
 * <p>
 * Ignored request configuration.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Data
@Slf4j
@ConfigurationProperties(prefix = "jwt.configuration")
public class JwtConfiguration {
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Key prefix of JWT stored in Redis.
     */
    @Setter(AccessLevel.NONE)
    private String jwtRedisKeyPrefix;

    public JwtConfiguration(MafProjectProperty mafProjectProperty) {
        this.signingKey = String.format("%s %s", mafProjectProperty.getProjectParentArtifactId(), mafProjectProperty.getVersion());
        log.info("Initiated JWT signing key: {}. The specified key byte array is {} bits", this.signingKey,
                 this.signingKey.getBytes(StandardCharsets.UTF_8).length * 8);
        jwtRedisKeyPrefix = String.format("%s:jwt:", mafProjectProperty.getProjectParentArtifactId());
        log.warn("Initiated 'jwtRedisKeyPrefix': {}", jwtRedisKeyPrefix);
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
