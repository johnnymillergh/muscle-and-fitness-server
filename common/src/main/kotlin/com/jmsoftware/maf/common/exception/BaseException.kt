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
open class BaseException(
    /**
     * Message is REQUIRED. Default message is: Error. A generic status for an error in the server itself.
     */
    override val message: String = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
    /**
     * Code is REQUIRED. Default code is 500 (Internal Server Error).
     */
    val code: Int = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    val data: Any? = null
) : Exception(message) {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 5049763892480652887L
    }
}
