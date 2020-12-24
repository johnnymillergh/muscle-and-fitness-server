package com.jmsoftware.maf.reactivespringbootstarter.util;


import cn.hutool.core.util.ObjectUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * <h1>RequestUtil</h1>
 * <p>
 * Request util.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/15/20 9:58 PM
 **/
public class RequestUtil {
    /**
     * Get request user's IP and port information. If the request is through reverse proxy, this method will not work
     * out.
     *
     * @param request HTTP request.
     * @return user's IP and port information. Pattern like: IP:Port.
     * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
     * @date 2/15/20 9:52 PM
     */
    public static String getRequesterIpAndPort(ServerHttpRequest request) {
        var requestRemoteAddress = request.getRemoteAddress();
        if (ObjectUtil.isNotNull(requestRemoteAddress)) {
            return requestRemoteAddress.toString().substring(1);
        }
        return "Unknown Requester";
    }
}
