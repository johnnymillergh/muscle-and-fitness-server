package com.jmsoftware.maf.springcloudstarter.util;

import com.jmsoftware.maf.common.constant.MafHttpHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Description: UserUtil, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/28/2021 1:40 PM
 **/
public class UserUtil {
    private UserUtil() {
    }

    public static String getCurrentUsername() {
        final ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest().getHeader(MafHttpHeader.X_USERNAME.getHeader());
    }

    public static Long getCurrentId() {
        final ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return Long.valueOf(servletRequestAttributes.getRequest().getHeader(MafHttpHeader.X_ID.getHeader()));
    }
}
