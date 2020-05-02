package com.jmsoftware.authcenter.universal.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.authcenter.universal.Constants;
import com.jmsoftware.authcenter.universal.configuration.JwtConfiguration;
import com.jmsoftware.authcenter.universal.configuration.ProjectProperty;
import com.jmsoftware.authcenter.universal.service.RedisService;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.BaseException;
import com.jmsoftware.common.exception.SecurityException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
public class JwtUtil {
    private final JwtConfiguration jwtConfiguration;
    private final RedisService redisService;
    private final ProjectProperty projectProperty;

    /**
     * Create JWT.
     *
     * @param rememberMe Remember me
     * @param id         User id
     * @param subject    Username
     * @param roles      Roles
     * @return JWT
     */
    @SuppressWarnings("unused")
    public String createJwt(Boolean rememberMe,
                            Long id,
                            String subject,
                            List<String> roles) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setId(id.toString())
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, jwtConfiguration.getSigningKey())
                .claim("roles", roles);

        // Set expire duration of JWT.
        Long ttl = rememberMe ? jwtConfiguration.getTtlForRememberMe() : jwtConfiguration.getTtl();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }

        String jwt = builder.compact();
        // Store new JWT in Redis
        Boolean result = redisService.set(Constants.REDIS_JWT_KEY_PREFIX + subject, jwt, ttl, TimeUnit.MILLISECONDS);
        if (result) {
            return jwt;
        }
        throw new BaseException(HttpStatus.ERROR);
    }

    /**
     * Parse JWT.
     *
     * @param jwt JWT
     * @return {@link Claims}
     */
    public Claims parseJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfiguration.getSigningKey())
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            String redisKey = projectProperty.getProjectArtifactId() + ":jwt:" + username;

            // Check if JWT exists
            Long expire = redisService.getExpire(redisKey, TimeUnit.MILLISECONDS);
            if (Objects.isNull(expire) || expire <= 0) {
                throw new SecurityException(HttpStatus.TOKEN_EXPIRED);
            }

            // Check if current JWT is equal to the one in Redis.
            // If it's noe equal, that indicates current user has signed out or logged in before.
            // Both situations reveal the JWT expired.
            String redisToken = redisService.get(redisKey);
            if (!StrUtil.equals(jwt, redisToken)) {
                throw new SecurityException(HttpStatus.TOKEN_OUT_OF_CONTROL);
            }
            return claims;
        } catch (ExpiredJwtException e) {
            log.error("Token expired.");
            throw new SecurityException(HttpStatus.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("Token not supported.");
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        } catch (MalformedJwtException e) {
            log.error("Invalid token.");
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        } catch (SignatureException e) {
            log.error("Invalid signature of token.");
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("Token parameter not exists.");
            throw new SecurityException(HttpStatus.TOKEN_PARSE_ERROR);
        }
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
