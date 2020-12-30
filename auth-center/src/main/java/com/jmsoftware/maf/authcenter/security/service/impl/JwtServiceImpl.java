package com.jmsoftware.maf.authcenter.security.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.authcenter.universal.configuration.JwtConfiguration;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtPayload;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.domain.authcenter.security.UserPrincipal;
import com.jmsoftware.maf.common.exception.SecurityException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <h1>JwtServiceImpl</h1>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-03 13:40
 **/
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class JwtServiceImpl implements JwtService {
    private final JwtConfiguration jwtConfiguration;
    private final RedisTemplate<Object, Object> redisTemplate;
    private SecretKey secretKey;
    private JwtParser jwtParser;

    @PostConstruct
    private void init() {
        log.info("Start to init class members of {}.", this.getClass().getSimpleName());
        secretKey = Keys.hmacShaKeyFor(jwtConfiguration.getSigningKey().getBytes(StandardCharsets.UTF_8));
        log.warn("Secret key for JWT was generated. Algorithm: {}", secretKey.getAlgorithm());
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    @Override
    public String createJwt(Authentication authentication, Boolean rememberMe) {
        val userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createJwt(rememberMe, userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRoles(),
                         userPrincipal.getAuthorities());
    }

    @Override
    public String createJwt(Boolean rememberMe, Long id, String subject, List<String> roles,
                            Collection<? extends GrantedAuthority> authorities) {
        val now = new Date();
        val builder = Jwts.builder()
                .setId(id.toString())
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(secretKey)
                .claim("roles", roles);
        // Don't generate authority information in JWT.
        //  .claim("authorities", authorities)
        // Set expire duration of JWT.
        val ttl = rememberMe ? jwtConfiguration.getTtlForRememberMe() : jwtConfiguration.getTtl();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }
        val jwt = builder.compact();
        // Store new JWT in Redis
        String redisKeyOfJwt = String.format("%s%s", jwtConfiguration.getJwtRedisKeyPrefix(), subject);
        redisTemplate.opsForValue().set(redisKeyOfJwt, jwt, ttl, TimeUnit.MILLISECONDS);
        log.info("Storing JWT in Redis. Key: {}, Value: {}", redisKeyOfJwt, jwt);
        return jwt;
    }

    @Override
    public Claims parseJwt(String jwt) throws SecurityException {
        Claims claims;
        try {
            claims = Optional.ofNullable(jwtParser.parseClaimsJws(jwt).getBody())
                    .orElseThrow(() -> new SecurityException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                             "The JWT Claims Set is null", null));
        } catch (ExpiredJwtException e) {
            log.error("JWT is expired. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.UNAUTHORIZED, "JWT is expired (JWT itself)");
        } catch (UnsupportedJwtException e) {
            log.error("JWT is unsupported. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.UNAUTHORIZED, "JWT is unsupported");
        } catch (MalformedJwtException e) {
            log.error("JWT is invalid. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.UNAUTHORIZED, "JWT is invalid");
        } catch (IllegalArgumentException e) {
            log.error("The parameter of JWT is invalid. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.UNAUTHORIZED, "The parameter of JWT is invalid");
        }
        val username = claims.getSubject();
        val redisKeyOfJwt = jwtConfiguration.getJwtRedisKeyPrefix() + username;
        // Check if JWT exists
        val expire = redisTemplate.opsForValue().getOperations().getExpire(redisKeyOfJwt, TimeUnit.MILLISECONDS);
        if (ObjectUtil.isNull(expire) || expire <= 0) {
            throw new SecurityException(HttpStatus.UNAUTHORIZED, "JWT is expired (Redis expiration)");
        }
        // Check if the current JWT is equal to the one in Redis.
        // If it's noe equal, that indicates current user has signed out or logged in before.
        // Both situations reveal the JWT has expired.
        val jwtInRedis = (String) redisTemplate.opsForValue().get(redisKeyOfJwt);
        if (!StrUtil.equals(jwt, jwtInRedis)) {
            throw new SecurityException(HttpStatus.UNAUTHORIZED, "JWT is expired (Not equaled)");
        }
        return claims;
    }

    @Override
    public void invalidateJwt(HttpServletRequest request) throws SecurityException {
        val jwt = getJwtFromRequest(request);
        val username = getUsernameFromJwt(jwt);
        // Delete JWT from redis
        String redisKeyOfJwt = String.format("%s%s", jwtConfiguration.getJwtRedisKeyPrefix(), username);
        val deletedKeyNumber = redisTemplate.opsForValue().getOperations().delete(redisKeyOfJwt);
        log.error("Invalidate JWT. Redis key of JWT = {}, deleted = {}", redisKeyOfJwt, deletedKeyNumber);
    }

    @Override
    public String getUsernameFromJwt(String jwt) throws SecurityException {
        val claims = parseJwt(jwt);
        return claims.getSubject();
    }

    @Override
    public String getUsernameFromRequest(HttpServletRequest request) throws SecurityException {
        val jwt = this.getJwtFromRequest(request);
        return this.getUsernameFromJwt(jwt);
    }

    @Override
    public String getJwtFromRequest(HttpServletRequest request) {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(JwtConfiguration.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtConfiguration.TOKEN_PREFIX.length());
        }
        return null;
    }

    @Override
    public ParseJwtResponse parse(@Valid ParseJwtPayload payload) throws SecurityException {
        String usernameFromJwt = this.getUsernameFromJwt(payload.getJwt());
        ParseJwtResponse response = new ParseJwtResponse();
        response.setUsername(usernameFromJwt);
        return response;
    }
}
