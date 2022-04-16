package com.jmsoftware.maf.common.exception

import org.springframework.http.HttpStatus
import java.io.Serial

/**
 * <h1>ResourceNotFoundException</h1>
 *
 *
 * Resource not found exception
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/19/2022 5:05 PM
 */
class ResourceNotFoundException : BaseException {
    constructor(message: String) : super(HttpStatus.NOT_FOUND, message)

    companion object {
        @Serial
        private val serialVersionUID = -1706570460684455863L
    }
}
