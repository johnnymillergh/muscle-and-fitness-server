package com.jmsoftware.maf.common.exception

import org.springframework.http.HttpStatus
import java.io.Serial

/**
 * # ResourceNotFoundException
 *
 * Resource not found exception
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:18 PM
 */
class ResourceNotFoundException(message: String) : BaseException(message, HttpStatus.NOT_FOUND.value()) {
    companion object {
        @Serial
        private const val serialVersionUID = -1706570460684455863L
    }
}
