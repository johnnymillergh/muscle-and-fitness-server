package com.jmsoftware.apiportal.universal.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.apiportal.universal.configuration.Constants;
import com.jmsoftware.apiportal.universal.configuration.JwtConfiguration;
import com.jmsoftware.apiportal.universal.domain.UserPrincipal;
import com.jmsoftware.apiportal.universal.service.RedisService;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.BaseException;
import com.jmsoftware.common.exception.SecurityException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

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
 * <h1>JwtUtil</h1>
 * <p>JWT util</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-03 13:40
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class JwtUtil {
    private final JwtConfiguration jwtConfiguration;
    private final RedisService redisService;
    private SecretKey secretKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        log.info("Start to init class members of {}.", this.getClass().getSimpleName());
        secretKey = Keys.hmacShaKeyFor(jwtConfiguration.getSigningKey().getBytes(StandardCharsets.UTF_8));
        log.warn("Secret key for JWT was generated. Algorithm: {}", secretKey.getAlgorithm());
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    /**
     * Create JWT.
     *
     * @param rememberMe  Remember me
     * @param id          User id
     * @param subject     Username
     * @param roles       Roles
     * @param authorities Granted Authority
     * @return JWT
     */
    public String createJwt(Boolean rememberMe,
                            Long id,
                            String subject,
                            List<String> roles,
                            Collection<? extends GrantedAuthority> authorities) {
        var now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setId(id.toString())
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(secretKey)
                .claim("roles", roles);
        // TODO: Don't generate authority information in JWT.
        //  .claim("authorities", authorities)
        // Set expire duration of JWT.
        Long ttl = rememberMe ? jwtConfiguration.getTtlForRememberMe() : jwtConfiguration.getTtl();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }
        String jwt = builder.compact();
        // Store new JWT in Redis
        var result = redisService.set(Constants.REDIS_JWT_KEY_PREFIX + subject, jwt, ttl,
                                      TimeUnit.MILLISECONDS);
        if (result) {
            return jwt;
        } else {
            throw new BaseException(HttpStatus.ERROR, "Cannot persist JWT into Redis");
        }
    }

    /**
     * Create JWT.
     *
     * @param authentication Authentication information
     * @param rememberMe     Remember me
     * @return JWT
     */
    public String createJwt(Authentication authentication, Boolean rememberMe) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createJwt(rememberMe, userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRoles(),
                         userPrincipal.getAuthorities());
    }

    /**
     * Parse JWT.
     *
     * @param jwt JWT
     * @return {@link Claims}
     */
    public Claims parseJwt(String jwt) {
        Claims claims;
        try {
            claims = Optional.ofNullable(jwtParser.parseClaimsJws(jwt).getBody())
                    .orElseThrow(() -> new BaseException(HttpStatus.TOKEN_PARSE_ERROR, "The JWT Claims Set is null"));
        } catch (ExpiredJwtException e) {
            log.error("JWT is expired. JWT:{}, message: {}", jwt, e.getMessage());
            throw new SecurityException(HttpStatus.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("JWT is unsupported. JWT:{}, message: {}", jwt, e.getMessage());
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        } catch (MalformedJwtException e) {
            log.error("JWT is invalid. JWT:{}, message: {}", jwt, e.getMessage());
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("The parameter of JWT is invalid. JWT:{}, message: {}", jwt, e.getMessage());
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        }
        String username = claims.getSubject();
        String redisKeyOfJwt = Constants.REDIS_JWT_KEY_PREFIX + username;
        // Check if JWT exists
        Long expire = redisService.getExpire(redisKeyOfJwt, TimeUnit.MILLISECONDS);
        if (ObjectUtil.isNull(expire) || expire <= 0) {
            throw new SecurityException(HttpStatus.TOKEN_EXPIRED);
        }
        // Check if the current JWT is equal to the one in Redis.
        // If it's noe equal, that indicates current user has signed out or logged in before.
        // Both situations reveal the JWT has expired.
        String jwtInRedis = redisService.get(redisKeyOfJwt);
        if (!StrUtil.equals(jwt, jwtInRedis)) {
            throw new SecurityException(HttpStatus.TOKEN_OUT_OF_CONTROL);
        }
        return claims;
    }

    /**
     * Invalidate JWT.
     *
     * @param request Request
     */
    public void invalidateJwt(HttpServletRequest request) {
        String jwt = getJwtFromRequest(request);
        String username = getUsernameFromJwt(jwt);
        // Delete JWT from redis
        Long deleted = redisService.delete(Constants.REDIS_JWT_KEY_PREFIX + username);
        log.error("Invalidate JWT. Username = {}, deleted = {}", username, deleted);
    }

    /**
     * Get username from JWT.
     *
     * @param jwt JWT
     * @return Username
     */
    public String getUsernameFromJwt(String jwt) {
        Claims claims = parseJwt(jwt);
        return claims.getSubject();
    }

    /**
     * Get username from HTTP request.
     *
     * @param request HTTP request
     * @return username
     */
    public String getUsernameFromRequest(HttpServletRequest request) {
        String jwt = this.getJwtFromRequest(request);
        return this.getUsernameFromJwt(jwt);
    }

    /**
     * Get JWT from request's header.
     *
     * @param request request
     * @return JWT
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.REQUEST_TOKEN_KEY);
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(Constants.JWT_PREFIX)) {
            return bearerToken.substring(Constants.JWT_PREFIX.length());
        }
        return null;
    }
}
