package com.jmsoftware.maf.springcloudstarter.util

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.constant.MafHttpHeader.X_ID
import com.jmsoftware.maf.common.constant.MafHttpHeader.X_USERNAME
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.util.UserUtil.log
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * # UserUtil
 *
 * Description: UserUtil, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:54 AM
 */
internal object UserUtil {
    val log = logger()
}

/**
 * Gets current username. Never throw exceptions.
 *
 * @return the current id; null if the current request is ignored by api-gateway
 */
fun currentUsername(): String? {
    val servletRequestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
    val request = servletRequestAttributes.request
    val currentUsername = request.getHeader(X_USERNAME.header)
    if (StrUtil.isBlank(currentUsername)) {
        log.warn("Found blank $X_USERNAME in the header of current request [${request.method}] ${request.requestURI}")
        return null
    }
    return currentUsername
}

/**
 * Gets current user's id. Never throw exceptions.
 *
 * @return the current id; null if the current request is ignored by api-gateway
 */
fun currentUserId(): Long? {
    val servletRequestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
    val request = servletRequestAttributes.request
    val xid = request.getHeader(X_ID.header)
    if (StrUtil.isBlank(xid)) {
        log.warn("Found blank `$X_ID` in the header of current request [${request.method}] ${request.requestURI}")
        return null
    }
    return xid.toLong()
}
