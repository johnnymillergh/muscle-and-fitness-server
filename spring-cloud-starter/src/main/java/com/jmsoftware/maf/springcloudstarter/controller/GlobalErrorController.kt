package com.jmsoftware.maf.springcloudstarter.controller

import com.jmsoftware.maf.common.util.logger
import lombok.extern.slf4j.Slf4j
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * <h1>ErrorController</h1>
 *
 *
 * Error controller.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 16:56
 */
@Slf4j
@RestController
class GlobalErrorController(
    errorAttributes: ErrorAttributes,
    serverProperties: ServerProperties,
    errorViewResolvers: List<ErrorViewResolver>
) : BasicErrorController(errorAttributes, serverProperties.error, errorViewResolvers) {
    companion object {
        private val log = logger()
    }

    override fun error(request: HttpServletRequest): ResponseEntity<Map<String, Any>> {
        val httpStatus = getStatus(request)
        val body = getErrorAttributes(request, ErrorAttributeOptions.defaults())
        body["message"] = httpStatus.reasonPhrase
        body["trace"]?.let {
            val message = body["message"]
            val firstLineOfTrace = it.toString().split("\\n".toRegex())[0]
            val joinedMessage = "$message $firstLineOfTrace"
            body["message"] = joinedMessage
            body["trace"] = "Trace has been simplified by ${this.javaClass.name}. Refer to `message`"
        }
        log.error("Captured HTTP request error. Response body = {}", body)
        return ResponseEntity(body, httpStatus)
    }
}
