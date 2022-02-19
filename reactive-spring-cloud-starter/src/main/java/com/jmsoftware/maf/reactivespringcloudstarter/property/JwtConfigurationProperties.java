package com.jmsoftware.maf.reactivespringcloudstarter.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * <h1>JwtConfigurationProperties</h1>
 * <p>
 * Ignored request configuration.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/2/20 11:41 PM
 **/
@Data
@Slf4j
@Validated
@ConfigurationProperties(prefix = JwtConfigurationProperties.PREFIX)
public class JwtConfigurationProperties {
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

    public JwtConfigurationProperties(MafProjectProperties mafProjectProperties) {
        this.signingKey = format("{}:{}", mafProjectProperties.getGroupId(),
                                 mafProjectProperties.getProjectParentArtifactId());
        log.info("Initiated JWT signing key: {}. The specified key byte array is {} bits", this.signingKey,
                 this.signingKey.getBytes(StandardCharsets.UTF_8).length * 8);
        this.jwtRedisKeyPrefix = format("{}:jwt:", mafProjectProperties.getProjectParentArtifactId());
        log.warn("Initiated 'jwtRedisKeyPrefix': {}", this.jwtRedisKeyPrefix);
    }
}
