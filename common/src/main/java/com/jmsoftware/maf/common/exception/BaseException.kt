package com.jmsoftware.maf.common.exception

import org.springframework.http.HttpStatus
import java.io.Serial

/**
 * # BaseException
 *
 * Base exception.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:20 PM
 */
open class BaseException : Exception {
    /**
     * Code is REQUIRED. Default code is 500 (Internal Server Error).
     */
    var code = HttpStatus.INTERNAL_SERVER_ERROR.value()

    /**
     * Message is REQUIRED. Default message is: Error. A generic status for an error in the server itself.
     */
    var msg: String = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
    var data: Any? = null

    constructor(httpStatus: HttpStatus) {
        this.code = httpStatus.value()
        msg = httpStatus.reasonPhrase
    }

    constructor(data: Any?, httpStatus: HttpStatus) : this(httpStatus) {
        this.data = data
    }

    constructor(message: String) {
        this.msg = message
    }

    constructor(status: Int, message: String, data: Any?) {
        this.code = status
        this.msg = message
        this.data = data
    }

    constructor(data: Any?) {
        this.data = data
    }

    constructor(throwable: Throwable) : this(throwable.message) {
        super.setStackTrace(throwable.stackTrace)
    }

    constructor(data: Any?, message: String) {
        this.data = data
        this.msg = message
    }

    companion object {
        @Serial
        private val serialVersionUID = 5049763892480652887L
    }
}
