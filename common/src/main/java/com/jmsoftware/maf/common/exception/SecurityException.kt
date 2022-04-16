package com.jmsoftware.maf.common.exception

import org.springframework.http.HttpStatus
import java.io.Serial

/**
 * <h1>SecurityException</h1>
 *
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date 3/12/20 3:15 PM
 */
class SecurityException : BaseException {
    constructor(httpStatus: HttpStatus) : super(httpStatus)
    constructor(httpStatus: HttpStatus, message: String) : super(httpStatus, message)
    constructor(httpStatus: HttpStatus, message: String, data: Any?) : super(httpStatus.value(), message, data)

    companion object {
        @Serial
        private val serialVersionUID = -767157443094687237L
    }
}
