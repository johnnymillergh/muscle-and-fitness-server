package com.jmsoftware.maf.springcloudstarter.filter

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.springcloudstarter.util.getRequestIpAndPort
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

/**
 * # RequestFilter
 *
 * Request filter.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 9:55 PM
 */
@Component
class AccessLogFilter(
    private val mafConfigurationProperties: MafConfigurationProperties,
) : OncePerRequestFilter() {
    companion object {
        private val log = logger()
    }

    private val antPathMatcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Ignore URL
        for (ignoredUrl in mafConfigurationProperties.flattenIgnoredUrls()) {
            if (antPathMatcher.match(ignoredUrl, request.requestURI)) {
                filterChain.doFilter(request, response)
                return
            }
        }
        log.info("The requester(${getRequestIpAndPort(request)}) requested resource. Request URL: [${request.method}] ${request.requestURL}")
        filterChain.doFilter(request, response)
    }
}
