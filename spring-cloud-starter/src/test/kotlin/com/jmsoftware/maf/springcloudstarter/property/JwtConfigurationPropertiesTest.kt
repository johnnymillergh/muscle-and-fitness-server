package com.jmsoftware.maf.springcloudstarter.property

import com.jmsoftware.maf.common.util.logger
import jakarta.annotation.Resource

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.text.Charsets.UTF_8

/**
 * # JwtConfigurationPropertiesTest
 *
 * Description: JwtConfigurationPropertiesTest, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 6/23/2023 4:29 PM
 */
@ExtendWith(SpringExtension::class)
@EnableConfigurationProperties(value = [JwtConfigurationProperties::class])
@TestPropertySource(
    properties = [
        "maf.configuration.jwt.signingKey=com.jmsoftware.maf:muscle-and-fitness-server:jwt-signing-key",
        "maf.configuration.jwt.jwt-redis-key-prefix=com.jmsoftware.maf:muscle-and-fitness-server:",
    ]
)
internal class JwtConfigurationPropertiesTest {
    companion object {
        private var log = logger()
    }

    @Resource
    lateinit var properties: JwtConfigurationProperties

    /**
     * Test JwtConfigurationProperties.
     *
     * ```
     * 2023-06-23 16:25:38.385 ERROR [auth-center,64955702fe6b354ad44be0106833168f,aa89e9a281fd9c76] 50408 - [nio-8800-exec-9] c.j.m.s.aspect.CommonExceptionControllerAdvice   : Internal server exception occurred! Exception message: The verification key's size is 352 bits which is not secure enough for the HS384 algorithm.  The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HS384 MUST have a size >= 384 bits (the key size must be greater than or equal to the hash output size).  Consider using the io.jsonwebtoken.security.Keys class's 'secretKeyFor(SignatureAlgorithm.HS384)' method to create a key guaranteed to be secure enough for HS384.  See https://tools.ietf.org/html/rfc7518#section-3.2 for more information.
     * ```
     */
    @Test
    fun testJwtConfigurationProperties_whenSigningKeyIsLongEnough() {
        assertNotNull(properties)
        assertNotNull(properties.signingKey)
        assertNotNull(properties.jwtRedisKeyPrefix)
        val bitsOfSigningKey = properties.signingKey.toByteArray(UTF_8).size * 8
        log.info("Signing key bits: $bitsOfSigningKey, signing key: ${properties.signingKey}")
        assertTrue(bitsOfSigningKey >= 384)
    }
}
