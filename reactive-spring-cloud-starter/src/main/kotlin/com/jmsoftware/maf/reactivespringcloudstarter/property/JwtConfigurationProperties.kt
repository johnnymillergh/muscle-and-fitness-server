package com.jmsoftware.maf.reactivespringcloudstarter.property

import com.jmsoftware.maf.common.util.logger
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import java.nio.charset.StandardCharsets
import javax.validation.constraints.NotNull

/**
 * # JwtConfigurationProperties
 *
 * Ignored request configuration.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 9:41 PM
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = JwtConfigurationProperties.PREFIX)
class JwtConfigurationProperties(mafProjectProperties: MafProjectProperties) {
    companion object {
        /**
         * The constant PREFIX.
         */
        const val PREFIX = "maf.configuration.jwt"
        const val TOKEN_PREFIX = "Bearer "
        private val log = logger()
    }

    /**
     * Key prefix of JWT stored in Redis.
     */
    final val jwtRedisKeyPrefix: String

    /**
     * JWT signing key. Pattern: [groupId]:[projectParentArtifactId]@[version]
     */
    final val signingKey: String

    /**
     * Time to live of JWT. Default: 3 * 600000 milliseconds (1800000 ms, 30 min).
     */
    var ttl: @NotNull Long = 3 * 600000L

    /**
     * Time to live of JWT for remember me, Default: 7 * 86400000 milliseconds (604800000 ms, 7 day)
     */
    var ttlForRememberMe: @NotNull Long = 7 * 86400000L

    init {
        signingKey =
            "${mafProjectProperties.groupId}:${mafProjectProperties.projectParentArtifactId}@${mafProjectProperties.version}"
        log.info(
            "Initiated JWT signing key: $signingKey. The specified key byte array is ${
                signingKey.toByteArray(StandardCharsets.UTF_8).size * 8
            } bits"
        )
        jwtRedisKeyPrefix = "${mafProjectProperties.projectParentArtifactId}:jwt:"
        log.warn("Initiated `jwtRedisKeyPrefix`: $jwtRedisKeyPrefix")
    }
}
