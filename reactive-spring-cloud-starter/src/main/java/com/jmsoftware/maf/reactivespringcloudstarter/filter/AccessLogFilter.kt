package com.jmsoftware.maf.reactivespringcloudstarter.filter

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafConfigurationProperties
import com.jmsoftware.maf.reactivespringcloudstarter.util.getRequesterIpAndPort
import org.springframework.core.Ordered
import org.springframework.util.AntPathMatcher
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 * # AccessLogFilter
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/17/22 8:01 AM
 */
class AccessLogFilter(
    private val mafConfigurationProperties: MafConfigurationProperties,
) : WebFilter, Ordered {
    companion object {
        private val log = logger()
    }

    private val antPathMatcher = AntPathMatcher()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        for (ignoredUrl in mafConfigurationProperties.flattenIgnoredUrls()) {
            if (antPathMatcher.match(ignoredUrl, request.uri.path)) {
                return chain.filter(exchange)
            }
        }
        // Only record non-ignored request log
        log.info("${this.javaClass.simpleName} (pre). Requester: ${getRequesterIpAndPort(request)}, request URL: [${request.method}] ${request.uri}")
        return chain.filter(exchange).then(
            Mono.fromRunnable {
                log.info("${this.javaClass.simpleName} (post). Requester: ${getRequesterIpAndPort(request)}, request URL: [${request.method}] ${request.uri}")
            }
        )
    }

    override fun getOrder(): Int {
        return -500
    }
}
