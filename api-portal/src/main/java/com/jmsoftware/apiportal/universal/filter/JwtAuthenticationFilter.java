package com.jmsoftware.apiportal.universal.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.jmsoftware.apiportal.universal.configuration.CustomConfiguration;
import com.jmsoftware.apiportal.universal.service.impl.CustomUserDetailsServiceImpl;
import com.jmsoftware.apiportal.universal.service.impl.JwtServiceImpl;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.SecurityException;
import com.jmsoftware.common.util.RequestUtil;
import com.jmsoftware.common.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * <h1>JwtAuthenticationFilter</h1>
 * <p>Jwt Authentication Filter.</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:24
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    private final JwtServiceImpl jwtServiceImpl;
    private final CustomConfiguration customConfiguration;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT authentication is filtering [{}] client requested access. URL: {}, HTTP method: {}",
                 RequestUtil.getRequestIpAndPort(request), request.getServletPath(), request.getMethod());
        if (customConfiguration.getWebSecurityDisabled()) {
            log.warn("The web security is disabled! Might face severe web security issue.");
            filterChain.doFilter(request, response);
            return;
        }
        if (checkIgnores(request)) {
            log.info("The request can be ignored. URL: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = jwtServiceImpl.getJwtFromRequest(request);
        if (StrUtil.isBlank(jwt)) {
            log.error("Invalid JWT, the JWT of request is empty.");
            ResponseUtil.renderJson(response, HttpStatus.UNAUTHORIZED, null);
            return;
        }
        String username;
        try {
            username = jwtServiceImpl.getUsernameFromJwt(jwt);
        } catch (SecurityException e) {
            log.error("Exception occurred when getting username from JWT. Exception message: {} JWT: {}",
                      e.getMessage(), jwt);
            var httpStatus = HttpStatus.fromCode(e.getCode());
            ResponseUtil.renderJson(response, httpStatus, null);
            return;
        }
        UserDetails userDetails;
        try {
            userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            log.error("Cannot find user by username: {}", username);
            ResponseUtil.renderJson(response, HttpStatus.UNAUTHORIZED, null);
            return;
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        log.info("JWT authentication passed! Authentication: {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    /**
     * Check if current request needs to be ignored. (Permission interception)
     *
     * @param request Current request
     * @return true - Ignored, false - Not ignored
     */
    private boolean checkIgnores(HttpServletRequest request) {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (ObjectUtil.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }
        Set<String> ignoredRequestSet = Sets.newHashSet();
        HttpMethod finalHttpMethod = httpMethod;
        Optional.ofNullable(customConfiguration.getIgnoredRequest())
                .ifPresentOrElse((ignoredRequest -> {
                    ignoredRequestSet.addAll(ignoredRequest.getPattern());
                    switch (finalHttpMethod) {
                        case GET:
                            ignoredRequestSet.addAll(ignoredRequest.getGet());
                            break;
                        case PUT:
                            ignoredRequestSet.addAll(ignoredRequest.getPut());
                            break;
                        case HEAD:
                            ignoredRequestSet.addAll(ignoredRequest.getHead());
                            break;
                        case POST:
                            ignoredRequestSet.addAll(ignoredRequest.getPost());
                            break;
                        case PATCH:
                            ignoredRequestSet.addAll(ignoredRequest.getPatch());
                            break;
                        case TRACE:
                            ignoredRequestSet.addAll(ignoredRequest.getTrace());
                            break;
                        case DELETE:
                            ignoredRequestSet.addAll(ignoredRequest.getDelete());
                            break;
                        case OPTIONS:
                            ignoredRequestSet.addAll(ignoredRequest.getOptions());
                            break;
                        default:
                            break;
                    }
                }), () -> log.warn("Security warning: Ignored request is empty! The ignored request configuration " +
                                   "might be invalid!"));
        if (CollUtil.isNotEmpty(ignoredRequestSet)) {
            for (String ignore : ignoredRequestSet) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
