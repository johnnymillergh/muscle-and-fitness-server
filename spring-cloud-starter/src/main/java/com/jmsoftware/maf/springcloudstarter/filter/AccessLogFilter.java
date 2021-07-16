package com.jmsoftware.maf.springcloudstarter.filter;

import com.jmsoftware.maf.springcloudstarter.configuration.MafConfiguration;
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h1>RequestFilter</h1>
 * <p>Request filter.</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:24
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter extends OncePerRequestFilter {
    private final MafConfiguration mafConfiguration;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        // Ignore URL
        for (String ignoredUrl : this.mafConfiguration.flattenIgnoredUrls()) {
            if (this.antPathMatcher.match(ignoredUrl, request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        log.info("The requester({}) requested resource. Request URL: [{}] {}", RequestUtil.getRequestIpAndPort(request),
                 request.getMethod(), request.getRequestURL());
        filterChain.doFilter(request, response);
    }
}
