package com.jmsoftware.maf.springcloudstarter.util

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.constant.MafHttpHeader
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
    val currentUsername = request.getHeader(MafHttpHeader.X_USERNAME.header)
    if (StrUtil.isBlank(currentUsername)) {
        log.warn(
            "Found blank {} in the header of current request [{}] {}", MafHttpHeader.X_USERNAME,
            request.method, request.requestURI
        )
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
    val xid = request.getHeader(MafHttpHeader.X_ID.header)
    if (StrUtil.isBlank(xid)) {
        log.warn("Found blank `${MafHttpHeader.X_ID}` in the header of current request [${request.method}] ${request.requestURI}")
        return null
    }
    return java.lang.Long.valueOf(xid)
}
