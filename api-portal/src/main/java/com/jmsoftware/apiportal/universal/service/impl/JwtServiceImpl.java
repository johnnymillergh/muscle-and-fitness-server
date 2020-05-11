package com.jmsoftware.apiportal.universal.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.apiportal.universal.configuration.Constants;
import com.jmsoftware.apiportal.universal.configuration.JwtConfiguration;
import com.jmsoftware.apiportal.universal.domain.UserPrincipal;
import com.jmsoftware.apiportal.universal.service.JwtService;
import com.jmsoftware.apiportal.universal.service.RedisService;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.SecurityException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
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
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-03 13:40
 **/
@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class JwtServiceImpl implements JwtService {
    private final JwtConfiguration jwtConfiguration;
    private final RedisService redisService;
    private SecretKey secretKey;
    private JwtParser jwtParser;

    @PostConstruct
    private void init() {
        log.info("Start to init class members of {}.", this.getClass().getSimpleName());
        secretKey = Keys.hmacShaKeyFor(jwtConfiguration.getSigningKey().getBytes(StandardCharsets.UTF_8));
        log.warn("Secret key for JWT was generated. Algorithm: {}", secretKey.getAlgorithm());
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    @Override
    public String createJwt(Authentication authentication, Boolean rememberMe) throws SecurityException {
        val userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createJwt(rememberMe, userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRoles(),
                         userPrincipal.getAuthorities());
    }

    @Override
    public String createJwt(Boolean rememberMe, Long id, String subject, List<String> roles,
                            Collection<? extends GrantedAuthority> authorities) throws SecurityException {
        val now = new Date();
        val builder = Jwts.builder()
                .setId(id.toString())
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(secretKey)
                .claim("roles", roles);
        // TODO: Don't generate authority information in JWT.
        //  .claim("authorities", authorities)
        // Set expire duration of JWT.
        val ttl = rememberMe ? jwtConfiguration.getTtlForRememberMe() : jwtConfiguration.getTtl();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }
        val jwt = builder.compact();
        // Store new JWT in Redis
        val redisOperationResult = redisService.set(Constants.REDIS_JWT_KEY_PREFIX + subject, jwt, ttl,
                                      TimeUnit.MILLISECONDS);
        if (redisOperationResult) {
            return jwt;
        } else {
            throw new SecurityException(HttpStatus.ERROR, "Cannot persist JWT into Redis", null);
        }
    }

    @Override
    public Claims parseJwt(String jwt) throws SecurityException {
        Claims claims;
        try {
            claims = Optional.ofNullable(jwtParser.parseClaimsJws(jwt).getBody())
                    .orElseThrow(() -> new SecurityException(HttpStatus.TOKEN_PARSE_ERROR,
                                                             "The JWT Claims Set is null", null));
        } catch (ExpiredJwtException e) {
            log.error("JWT is expired. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("JWT is unsupported. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        } catch (MalformedJwtException e) {
            log.error("JWT is invalid. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("The parameter of JWT is invalid. Message: {} JWT: {}", e.getMessage(), jwt);
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        }
        val username = claims.getSubject();
        val redisKeyOfJwt = Constants.REDIS_JWT_KEY_PREFIX + username;
        // Check if JWT exists
        val expire = redisService.getExpire(redisKeyOfJwt, TimeUnit.MILLISECONDS);
        if (ObjectUtil.isNull(expire) || expire <= 0) {
            throw new SecurityException(HttpStatus.TOKEN_EXPIRED);
        }
        // Check if the current JWT is equal to the one in Redis.
        // If it's noe equal, that indicates current user has signed out or logged in before.
        // Both situations reveal the JWT has expired.
        val jwtInRedis = redisService.get(redisKeyOfJwt);
        if (!StrUtil.equals(jwt, jwtInRedis)) {
            throw new SecurityException(HttpStatus.TOKEN_OUT_OF_CONTROL);
        }
        return claims;
    }

    @Override
    public void invalidateJwt(HttpServletRequest request) throws SecurityException {
        val jwt = getJwtFromRequest(request);
        val username = getUsernameFromJwt(jwt);
        // Delete JWT from redis
        val deletedKeyNumber = redisService.delete(Constants.REDIS_JWT_KEY_PREFIX + username);
        log.error("Invalidate JWT. Username = {}, deleted = {}", username, deletedKeyNumber);
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
        val bearerToken = request.getHeader(Constants.REQUEST_TOKEN_KEY);
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(Constants.JWT_PREFIX)) {
            return bearerToken.substring(Constants.JWT_PREFIX.length());
        }
        return null;
    }
}
