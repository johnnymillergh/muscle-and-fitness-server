package com.jmsoftware.maf.springcloudstarter.property

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * # JwtConfigurationProperties
 *
 * Ignored request configuration.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 9:41 PM
 * @see [Kotlin and @Valid Spring Annotation](https://www.baeldung.com/kotlin/valid-spring-annotation)
 */
@Validated
@Configuration
@ConfigurationProperties(prefix = JwtConfigurationProperties.PREFIX)
class JwtConfigurationProperties {
    companion object {
        /**
         * The constant PREFIX.
         */
        const val PREFIX = "maf.configuration.jwt"
        const val TOKEN_PREFIX = "Bearer "
    }

    /**
     * Key prefix of JWT stored in Redis.
     */
    @field:NotBlank
    lateinit var jwtRedisKeyPrefix: String

    /**
     * JWT signing key. Pattern: [groupId]:[projectParentArtifactId]@[version]
     */
    @field:NotBlank
    lateinit var signingKey: String

    /**
     * Time to live of JWT. Default: 3 * 600000 milliseconds (1800000 ms, 30 min).
     */
    @field:NotNull
    var ttl: Long = 3 * 600_000L

    /**
     * Time to live of JWT for remember me, Default: 7 * 86400000 milliseconds (604800000 ms, 7 day)
     */
    @field:NotNull
    var ttlForRememberMe: Long = 7 * 86_400_000L
}
