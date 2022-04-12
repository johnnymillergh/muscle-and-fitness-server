package com.jmsoftware.maf.authcenter.security.service.impl

import cn.hutool.core.date.DateUtil
import cn.hutool.core.text.CharSequenceUtil
import cn.hutool.core.util.ObjectUtil
import com.jmsoftware.maf.authcenter.security.service.JwtService
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.JwtConfigurationProperties
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import lombok.RequiredArgsConstructor
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

/**
 * # JwtServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 9:27 PM
 */
@Service
@RequiredArgsConstructor
class JwtServiceImpl(
    private val jwtConfigurationProperties: JwtConfigurationProperties,
    private val redisTemplate: RedisTemplate<Any, Any>
) : JwtService {
    companion object {
        private val log = logger()
    }

    private lateinit var secretKey: SecretKey
    private lateinit var jwtParser: JwtParser

    @PostConstruct
    fun init() {
        log.info("Start to init class members of ${this.javaClass.simpleName}")
        secretKey = Keys.hmacShaKeyFor(
            jwtConfigurationProperties.signingKey.toByteArray(StandardCharsets.UTF_8)
        )
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build()
        log.warn("Secret key for JWT parser was generated. Algorithm: ${secretKey.algorithm}")
    }

    override fun createJwt(@NotNull authentication: Authentication, @NotNull rememberMe: Boolean): String {
        val userPrincipal = authentication.principal as UserPrincipal
        return this.createJwt(
            rememberMe,
            userPrincipal.id,
            userPrincipal.username,
            userPrincipal.roles,
            userPrincipal.authorities
        )
    }

    override fun createJwt(
        @NotNull rememberMe: Boolean,
        @NotNull id: Long,
        @NotNull subject: String,
        roles: List<String>,
        authorities: Collection<GrantedAuthority>
    ): String {
        val now = Date()
        val builder = Jwts.builder()
            .setId(id.toString())
            .setSubject(subject)
            .setIssuedAt(now)
            .signWith(secretKey)
            .claim("roles", roles)
        // Don't generate authority information in JWT.
        //  .claim("authorities", authorities)
        // Set expire duration of JWT.
        val ttl = if (rememberMe) jwtConfigurationProperties.ttlForRememberMe else jwtConfigurationProperties.ttl
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.toInt()))
        }
        val jwt = builder.compact()
        // Store new JWT in Redis
        val redisKeyOfJwt = "${jwtConfigurationProperties.jwtRedisKeyPrefix}$subject"
        redisTemplate.opsForValue()[redisKeyOfJwt, jwt, ttl] = TimeUnit.MILLISECONDS
        log.info("Storing JWT in Redis. Key: $redisKeyOfJwt, Value: $jwt")
        return jwt
    }

    override fun parseJwt(@NotBlank jwt: String): Claims {
        val claims: Claims = try {
            Optional.ofNullable(jwtParser.parseClaimsJws(jwt).body)
                .orElseThrow {
                    SecurityException(HttpStatus.INTERNAL_SERVER_ERROR, "The JWT Claims Set is null", null)
                }
        } catch (e: ExpiredJwtException) {
            log.error("JWT is expired. Message: ${e.message}, JWT: $jwt")
            throw SecurityException(HttpStatus.UNAUTHORIZED, "JWT is expired (JWT itself)")
        } catch (e: UnsupportedJwtException) {
            log.error("JWT is unsupported. Message: ${e.message}, JWT: $jwt")
            throw SecurityException(HttpStatus.UNAUTHORIZED, "JWT is unsupported")
        } catch (e: MalformedJwtException) {
            log.error("JWT is invalid. Message: ${e.message}, JWT: $jwt")
            throw SecurityException(HttpStatus.UNAUTHORIZED, "JWT is invalid")
        } catch (e: IllegalArgumentException) {
            log.error("The parameter of JWT is invalid. Message: ${e.message}, JWT: $jwt")
            throw SecurityException(HttpStatus.UNAUTHORIZED, "The parameter of JWT is invalid")
        }
        val username = claims.subject
        val redisKeyOfJwt = jwtConfigurationProperties.jwtRedisKeyPrefix + username
        // Check if JWT exists
        val expire = redisTemplate.opsForValue().operations.getExpire(redisKeyOfJwt, TimeUnit.MILLISECONDS)
        if (ObjectUtil.isNull(expire) || expire!! <= 0) {
            throw SecurityException(HttpStatus.UNAUTHORIZED, "JWT is expired (Redis expiration)")
        }
        // Check if the current JWT is equal to the one in Redis.
        // If it's noe equal, that indicates current user has signed out or logged in before.
        // Both situations reveal the JWT has expired.
        val jwtInRedis = redisTemplate.opsForValue()[redisKeyOfJwt] as String?
        if (!CharSequenceUtil.equals(jwt, jwtInRedis)) {
            throw SecurityException(HttpStatus.UNAUTHORIZED, "JWT is expired (Not equaled)")
        }
        return claims
    }

    override fun invalidateJwt(@NotNull request: HttpServletRequest) {
        val jwt = getJwtFromRequest(request)
        val username = getUsernameFromJwt(jwt)
        // Delete JWT from redis
        val redisKeyOfJwt = "${jwtConfigurationProperties.jwtRedisKeyPrefix}$username"
        val deletedKeyNumber = redisTemplate.opsForValue().operations.delete(redisKeyOfJwt)
        log.error("Invalidate JWT. Redis key of JWT = $redisKeyOfJwt, deleted = $deletedKeyNumber")
    }

    override fun getUsernameFromJwt(@NotBlank jwt: String): String {
        val claims = parseJwt(jwt)
        return claims.subject
    }

    override fun getUsernameFromRequest(@NotNull request: HttpServletRequest): String {
        val jwt = getJwtFromRequest(request)
        return getUsernameFromJwt(jwt)
    }

    override fun getJwtFromRequest(@NotNull request: HttpServletRequest): String {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (CharSequenceUtil.isNotBlank(bearerToken)
            && bearerToken.startsWith(JwtConfigurationProperties.TOKEN_PREFIX)
        ) {
            bearerToken.substring(JwtConfigurationProperties.TOKEN_PREFIX.length)
        } else null.toString()
    }

    override fun parse(@NotNull request: HttpServletRequest): ParseJwtResponse {
        val jwt = getJwtFromRequest(request)
        val claims = parseJwt(jwt)
        val parseJwtResponse = ParseJwtResponse()
        parseJwtResponse.id = claims.id.toLong()
        parseJwtResponse.username = claims.subject
        return parseJwtResponse
    }
}