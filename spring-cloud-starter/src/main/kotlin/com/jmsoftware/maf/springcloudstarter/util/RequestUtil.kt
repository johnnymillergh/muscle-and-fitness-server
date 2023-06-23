package com.jmsoftware.maf.springcloudstarter.util

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil.UNKNOWN
import jakarta.servlet.http.HttpServletRequest

/**
 * # RequestUtil
 *
 * Request util.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:35 AM
 */
object RequestUtil {
    const val UNKNOWN = "unknown"
}

/**
 * Get request user's IP and port information. If the request is through reverse proxy, this method will not work
 * out.
 *
 * @param request HTTP request.
 * @return user's IP and port information.
 */
fun getRequestIpAndPort(request: HttpServletRequest): String {
    var ip = request.getHeader("X-Real-IP")
    val port = request.remotePort
    if (!StrUtil.isBlank(ip) && !"".equals(ip, ignoreCase = true)) {
        return ip
    }
    ip = request.getHeader("X-Forwarded-For")
    return if (!StrUtil.isBlank(ip) && !UNKNOWN.equals(
            ip,
            ignoreCase = true
        )
    ) {
        // The first ip is actual ip
        val index = ip.indexOf(',')
        if (index != -1) {
            ip.substring(0, index) + ":" + port
        } else {
            "$ip:$port"
        }
    } else {
        request.remoteAddr + ":" + port
    }
}
