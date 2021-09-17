package com.jmsoftware.maf.springcloudstarter.util;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.common.constant.MafHttpHeader;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Description: UserUtil, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/28/2021 1:40 PM
 **/
@Slf4j
public class UserUtil {
    private UserUtil() {
    }

    /**
     * Gets current username. Never throw exceptions.
     *
     * @return the current id; null if the current request is ignored by api-gateway
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/17/21 7:58 AM
     */
    public static String getCurrentUsername() {
        val servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        val request = servletRequestAttributes.getRequest();
        val currentUsername = request.getHeader(MafHttpHeader.X_USERNAME.getHeader());
        if (StrUtil.isBlank(currentUsername)) {
            log.warn("Found blank {} in the header of current request [{}] {}", MafHttpHeader.X_USERNAME,
                     request.getMethod(), request.getRequestURI());
            return null;
        }
        return currentUsername;
    }

    /**
     * Gets current user's id. Never throw exceptions.
     *
     * @return the current id; null if the current request is ignored by api-gateway
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/17/21 7:58 AM
     */
    public static Long getCurrentId() {
        val servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        val request = servletRequestAttributes.getRequest();
        val xid = request.getHeader(MafHttpHeader.X_ID.getHeader());
        if (StrUtil.isBlank(xid)) {
            log.warn("Found blank {} in the header of current request [{}] {}", MafHttpHeader.X_ID, request.getMethod(),
                     request.getRequestURI());
            return null;
        }
        return Long.valueOf(xid);
    }
}
