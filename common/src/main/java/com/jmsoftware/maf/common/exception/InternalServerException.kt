package com.jmsoftware.maf.common.exception

import org.springframework.http.HttpStatus
import java.io.Serial

/**
 * # InternalServerException
 *
 * Business Exception
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:24 PM
 */
class InternalServerException : BaseException {
    constructor(httpStatus: HttpStatus) : super(httpStatus)
    constructor(httpStatus: HttpStatus, data: Any?) : super(data, httpStatus)
    constructor(httpStatus: HttpStatus, message: String) : super(httpStatus, message)
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)
    constructor(status: Int, message: String, data: Any?) : super(status, message, data)
    constructor(data: Any?) : super(data)
    constructor(data: Any?, message: String) : super(data, message)

    companion object {
        @Serial
        private val serialVersionUID = 6403325238832002908L
    }
}
