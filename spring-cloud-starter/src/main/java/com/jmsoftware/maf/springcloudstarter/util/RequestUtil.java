package com.jmsoftware.maf.springcloudstarter.util;


import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>RequestUtil</h1>
 * <p>
 * Request util.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/27/20 9:45 AM
 **/
public class RequestUtil {
    private static final String UNKNOWN = "unknown";

    private RequestUtil() {
    }

    /**
     * Get request user's IP and port information. If the request is through reverse proxy, this method will not work
     * out.
     *
     * @param request HTTP request.
     * @return user's IP and port information.
     */
    public static String getRequestIpAndPort(final HttpServletRequest request) {
        var ip = request.getHeader("X-Real-IP");
        final var port = request.getRemotePort();
        if (!StrUtil.isBlank(ip) && !"".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StrUtil.isBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            // The first ip is actual ip
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index) + ":" + port;
            } else {
                return ip + ":" + port;
            }
        } else {
            return request.getRemoteAddr() + ":" + port;
        }
    }
}
