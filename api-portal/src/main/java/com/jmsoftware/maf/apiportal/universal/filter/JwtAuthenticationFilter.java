package com.jmsoftware.maf.apiportal.universal.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.apiportal.universal.configuration.CustomConfiguration;
import com.jmsoftware.maf.apiportal.universal.service.impl.CustomUserDetailsServiceImpl;
import com.jmsoftware.maf.apiportal.universal.service.impl.JwtServiceImpl;
import com.jmsoftware.maf.common.constant.HttpStatus;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.common.util.RequestUtil;
import com.jmsoftware.maf.common.util.ResponseUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
import java.util.HashSet;
import java.util.Optional;

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
        val requesterIpAndPort = RequestUtil.getRequestIpAndPort(request);
        val method = request.getMethod();
        val requestUrl = request.getRequestURL();
        log.info("JWT authentication is filtering requester({}) access. Resource: [{}] {}",
                 requesterIpAndPort, method, requestUrl);
        if (customConfiguration.getWebSecurityDisabled()) {
            log.warn("The web security is disabled! Might face severe web security issue.");
            filterChain.doFilter(request, response);
            return;
        }
        if (checkIgnores(request)) {
            log.info("The resource can be ignored. Resource: [{}] {}", method, requestUrl);
            filterChain.doFilter(request, response);
            return;
        }
        val passedJwtAuthentication = doJwtAuthentication(request, response);
        if (!passedJwtAuthentication) {
            log.warn("The requester did not pass JWT authentication. Requester: {}. Resource: [{}] {}", requesterIpAndPort,
                     method, requestUrl);
            return;
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception occurred when filtering request. Exception message: {}", e.getMessage(), e);
            val exception = recursiveTraverseExceptionCause(e);
            if (exception instanceof SecurityException) {
                val code = ((SecurityException) exception).getCode();
                final var httpStatus = HttpStatus.fromCode(code);
                ResponseUtil.renderJson(response, httpStatus, exception.getMessage());
                return;
            }
            ResponseUtil.renderJson(response, HttpStatus.ERROR, e.getMessage());
        }
    }

    /**
     * Do JWT authentication. This method will not throw any exceptions.
     *
     * @param request  the request
     * @param response the response
     * @return the boolean
     * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
     * @date 5 /13/20 5:30 PM
     */
    private boolean doJwtAuthentication(HttpServletRequest request, HttpServletResponse response) {
        val jwt = jwtServiceImpl.getJwtFromRequest(request);
        if (StrUtil.isBlank(jwt)) {
            log.error("Invalid JWT, the JWT of request is empty.");
            ResponseUtil.renderJson(response, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getMessage());
            return false;
        }
        String username;
        try {
            username = jwtServiceImpl.getUsernameFromJwt(jwt);
        } catch (SecurityException e) {
            log.error("Exception occurred when getting username from JWT. Exception message: {} JWT: {}",
                      e.getMessage(), jwt);
            var httpStatus = HttpStatus.fromCode(e.getCode());
            ResponseUtil.renderJson(response, httpStatus, httpStatus.getMessage());
            return false;
        }
        UserDetails userDetails;
        try {
            userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
        } catch (Exception e) {
            log.error("Exception occurred when loading user by username! Exception message: {} Username: {}",
                      e.getMessage(), username);
            if (e instanceof UsernameNotFoundException) {
                ResponseUtil.renderJson(response, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getMessage());
                return false;
            }
            ResponseUtil.renderJson(response, HttpStatus.ERROR, e.getMessage());
            return false;
        }
        val authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        log.info("JWT authentication passed! Authentication: {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    /**
     * Check if current request needs to be ignored. (Permission interception)
     *
     * @param request Current request
     * @return true - Ignored, false - Not ignored
     */
    private boolean checkIgnores(HttpServletRequest request) {
        val method = request.getMethod();
        var httpMethod = HttpMethod.resolve(method);
        if (ObjectUtil.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }
        val ignoredRequestSet = new HashSet<String>();
        val finalHttpMethod = httpMethod;
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
            for (val ignore : ignoredRequestSet) {
                val matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recursive traverse exception cause exception.
     *
     * @param exception the exception
     * @return the exception
     * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
     * @date 5/13/20 4:51 PM
     */
    private Exception recursiveTraverseExceptionCause(@NonNull Exception exception) {
        if (ObjectUtil.isNotNull(exception.getCause())) {
            return recursiveTraverseExceptionCause((Exception) exception.getCause());
        }
        return exception;
    }
}
