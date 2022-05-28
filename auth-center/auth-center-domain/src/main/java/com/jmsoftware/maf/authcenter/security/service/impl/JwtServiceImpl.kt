package com.jmsoftware.maf.authcenter.security.service.impl

import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.authcenter.security.service.JwtService
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal
import com.jmsoftware.maf.common.exception.SecurityException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.JwtConfigurationProperties
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest

/**
 * # JwtServiceImpl
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/11/22 9:27 PM
 */
@Service
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

    override fun createJwt(authentication: Authentication, rememberMe: Boolean): String {
        val userPrincipal = authentication.principal as UserPrincipal
        return this.createJwt(
            rememberMe,
            userPrincipal.id!!,
            userPrincipal.usernameProperty!!,
            userPrincipal.roles!!,
            userPrincipal.authoritiesProperty!!
        )
    }

    override fun createJwt(
        rememberMe: Boolean,
        id: Long,
        subject: String,
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

    override fun parseJwt(jwt: String): Claims {
        val claims: Claims = try {
            Optional.ofNullable(jwtParser.parseClaimsJws(jwt).body)
                .orElseThrow {
                    SecurityException("The JWT Claims Set is null", INTERNAL_SERVER_ERROR)
                }
        } catch (e: ExpiredJwtException) {
            log.error("JWT is expired. Message: ${e.message}, JWT: $jwt")
            throw SecurityException("JWT is expired (JWT itself)", UNAUTHORIZED)
        } catch (e: UnsupportedJwtException) {
            log.error("JWT is unsupported. Message: ${e.message}, JWT: $jwt")
            throw SecurityException("JWT is unsupported", UNAUTHORIZED)
        } catch (e: MalformedJwtException) {
            log.error("JWT is invalid. Message: ${e.message}, JWT: $jwt")
            throw SecurityException("JWT is invalid", UNAUTHORIZED)
        } catch (e: IllegalArgumentException) {
            log.error("The parameter of JWT is invalid. Message: ${e.message}, JWT: $jwt")
            throw SecurityException("The parameter of JWT is invalid", UNAUTHORIZED)
        }
        val username = claims.subject
        val redisKeyOfJwt = jwtConfigurationProperties.jwtRedisKeyPrefix + username
        // Check if JWT exists
        val expire = redisTemplate.opsForValue().operations.getExpire(redisKeyOfJwt, TimeUnit.MILLISECONDS)
        if (ObjectUtil.isNull(expire) || expire!! <= 0) {
            throw SecurityException("JWT is expired (Redis expiration)", UNAUTHORIZED)
        }
        // Check if the current JWT is equal to the one in Redis.
        // If it's noe equal, that indicates current user has signed out or logged in before.
        // Both situations reveal the JWT has expired.
        val jwtInRedis = redisTemplate.opsForValue()[redisKeyOfJwt] as String?
        if (jwt != jwtInRedis) {
            throw SecurityException("JWT is expired (Not equaled)", UNAUTHORIZED)
        }
        return claims
    }

    override fun invalidateJwt(request: HttpServletRequest) {
        val jwt = getJwtFromRequest(request)
        val username = getUsernameFromJwt(jwt)
        // Delete JWT from redis
        val redisKeyOfJwt = "${jwtConfigurationProperties.jwtRedisKeyPrefix}$username"
        val deletedKeyNumber = redisTemplate.opsForValue().operations.delete(redisKeyOfJwt)
        log.error("Invalidate JWT. Redis key of JWT = $redisKeyOfJwt, deleted = $deletedKeyNumber")
    }

    override fun getUsernameFromJwt(jwt: String): String {
        val claims = parseJwt(jwt)
        return claims.subject
    }

    override fun getUsernameFromRequest(request: HttpServletRequest): String {
        val jwt = getJwtFromRequest(request)
        return getUsernameFromJwt(jwt)
    }

    override fun getJwtFromRequest(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (StrUtil.isNotBlank(bearerToken)
            && bearerToken.startsWith(JwtConfigurationProperties.TOKEN_PREFIX)
        ) {
            bearerToken.substring(JwtConfigurationProperties.TOKEN_PREFIX.length)
        } else null.toString()
    }

    override fun parse(request: HttpServletRequest): ParseJwtResponse {
        val jwt = getJwtFromRequest(request)
        val claims = parseJwt(jwt)
        val parseJwtResponse = ParseJwtResponse()
        parseJwtResponse.id = claims.id.toLong()
        parseJwtResponse.username = claims.subject
        return parseJwtResponse
    }
}
