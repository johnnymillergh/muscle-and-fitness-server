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

    public static String getCurrentUsername() {
        val servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest().getHeader(MafHttpHeader.X_USERNAME.getHeader());
    }

    /**
     * Gets current id.
     *
     * @return the current id
     * @throws IllegalArgumentException if X-ID is blank in HTTP header
     */
    public static Long getCurrentId() {
        val servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        val xid = servletRequestAttributes.getRequest().getHeader(MafHttpHeader.X_ID.getHeader());
        if (StrUtil.isBlank(xid)) {
            throw new IllegalArgumentException("Invalid X-ID");
        }
        return Long.valueOf(xid);
    }
}
