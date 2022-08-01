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
    constructor(httpStatus: HttpStatus) : super(httpStatus.reasonPhrase, httpStatus.value())
    constructor(httpStatus: HttpStatus, data: Any?) : super(httpStatus.reasonPhrase, httpStatus.value(), data)
    constructor(httpStatus: HttpStatus, message: String) : super(message, httpStatus.value())
    constructor(message: String) : super(message)
    constructor(status: Int, message: String, data: Any?) : super(message, status, data)
    constructor(data: Any?) : super(data = data)
    constructor(message: String, data: Any?) : super(message = message, data = data)

    companion object {
        @Serial
        private const val serialVersionUID = 6403325238832002908L
    }
}
