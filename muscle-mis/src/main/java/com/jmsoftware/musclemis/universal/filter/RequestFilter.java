package com.jmsoftware.musclemis.universal.filter;

import com.jmsoftware.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 14:24
 **/
@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter {
    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        log.info("[{}] Client requested access. Method: {}, URL: {}",
                 RequestUtil.getRequestIpAndPort(request),
                 request.getMethod(),
                 request.getRequestURL());
        filterChain.doFilter(request, response);
    }
}
