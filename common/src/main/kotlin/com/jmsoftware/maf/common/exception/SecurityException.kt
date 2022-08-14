package com.jmsoftware.maf.common.exception

import org.springframework.http.HttpStatus
import java.io.Serial

/**
 * # SecurityException
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:18 PM
 */
class SecurityException : BaseException {
    constructor(httpStatus: HttpStatus) : super(httpStatus.reasonPhrase, httpStatus.value())
    constructor(message: String, httpStatus: HttpStatus) : super(message, httpStatus.value())
    constructor(message: String, httpStatus: HttpStatus, data: Any?) : super(message, httpStatus.value(), data)

    companion object {
        @Serial
        private const val serialVersionUID = -767157443094687237L
    }
}
