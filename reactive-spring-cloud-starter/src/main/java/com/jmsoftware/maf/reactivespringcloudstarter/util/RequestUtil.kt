package com.jmsoftware.maf.reactivespringcloudstarter.util

import org.springframework.http.server.reactive.ServerHttpRequest

/**
 * # RequestUtil
 *
 * Request util.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/15/20 9:58 PM
 */
object RequestUtil

/**
 * Get request user's IP and port information. If the request is through reverse proxy, this method will not work
 * out.
 *
 * @param request HTTP request.
 * @return user's IP and port information. Pattern like: IP:Port.
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/15/20 9:52 PM
 */
fun getRequesterIpAndPort(request: ServerHttpRequest): String {
    val requestRemoteAddress = request.remoteAddress
    return requestRemoteAddress?.toString() ?: "Unknown Requester"
}
