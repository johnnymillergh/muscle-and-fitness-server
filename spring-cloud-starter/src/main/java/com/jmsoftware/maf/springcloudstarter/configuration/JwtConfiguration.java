package com.jmsoftware.maf.springcloudstarter.configuration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

/**
 * <h1>JwtConfiguration</h1>
 * <p>
 * Ignored request configuration.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 3/1/2021 10:28 AM
 **/
@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = JwtConfiguration.PREFIX)
public class JwtConfiguration {
    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "maf.configuration.jwt";
    public static final String TOKEN_PREFIX = "Bearer ";
    /**
     * Key prefix of JWT stored in Redis.
     */
    @Setter(AccessLevel.NONE)
    private String jwtRedisKeyPrefix;
    /**
     * JWT signing key. Pattern: [project-parent-artifact-id]@[version]
     */
    @Setter(AccessLevel.NONE)
    private String signingKey;
    /**
     * Time to live of JWT. Default: 3 * 600000 milliseconds (1800000 ms, 30 min).
     */
    @NotNull
    private Long ttl = 3 * 600000L;
    /**
     * Time to live of JWT for remember me, Default: 7 * 86400000 milliseconds (604800000 ms, 7 day)
     */
    @NotNull
    private Long ttlForRememberMe = 7 * 86400000L;
    public JwtConfiguration(MafProjectProperty mafProjectProperty) {
        this.signingKey = String.format("%s:%s@%s", mafProjectProperty.getProjectParentArtifactId(),
                                        mafProjectProperty.getProjectArtifactId(), mafProjectProperty.getVersion());
        log.info("Initiated JWT signing key: {}. The specified key byte array is {} bits", this.signingKey,
                 this.signingKey.getBytes(StandardCharsets.UTF_8).length * 8);
        this.jwtRedisKeyPrefix = String.format("%s:jwt:", mafProjectProperty.getProjectParentArtifactId());
        log.warn("Initiated 'jwtRedisKeyPrefix': {}", this.jwtRedisKeyPrefix);
    }
}
