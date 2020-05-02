package com.jmsoftware.apiportal.universal.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.jmsoftware.apiportal.universal.configuration.CustomConfiguration;
import com.jmsoftware.apiportal.universal.service.impl.CustomUserDetailsServiceImpl;
import com.jmsoftware.apiportal.universal.util.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final CustomConfiguration customConfiguration;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT authentication is filtering [{}] client requested access. URL: {}",
                 RequestUtil.getRequestIpAndPort(request),
                 request.getServletPath());

        // Check if disable Web Security.
        if (customConfiguration.getWebSecurityDisabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (checkIgnores(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtil.getJwtFromRequest(request);

        if (StrUtil.isNotBlank(jwt)) {
            try {
                String username = jwtUtil.getUsernameFromJwt(jwt);

                UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (SecurityException e) {
                ResponseUtil.renderJson(response, e);
            }
        } else {
            ResponseUtil.renderJson(response, HttpStatus.UNAUTHORIZED, null);
        }
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
        Set<String> ignores = Sets.newHashSet();
        HttpMethod finalHttpMethod = httpMethod;
        Optional.ofNullable(customConfiguration.getIgnoredRequest())
                .ifPresentOrElse((ignoredRequest -> {
                    ignores.addAll(ignoredRequest.getPattern());
                    switch (finalHttpMethod) {
                        case GET:
                            ignores.addAll(ignoredRequest.getGet());
                            break;
                        case PUT:
                            ignores.addAll(ignoredRequest.getPut());
                            break;
                        case HEAD:
                            ignores.addAll(ignoredRequest.getHead());
                            break;
                        case POST:
                            ignores.addAll(ignoredRequest.getPost());
                            break;
                        case PATCH:
                            ignores.addAll(ignoredRequest.getPatch());
                            break;
                        case TRACE:
                            ignores.addAll(ignoredRequest.getTrace());
                            break;
                        case DELETE:
                            ignores.addAll(ignoredRequest.getDelete());
                            break;
                        case OPTIONS:
                            ignores.addAll(ignoredRequest.getOptions());
                            break;
                        default:
                            break;
                    }
                }), () -> log.warn("Security warning: Ignored request is empty! The ignored request configuration " +
                                   "might be invalid!"));
        if (CollUtil.isNotEmpty(ignores)) {
            for (String ignore : ignores) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }

}
