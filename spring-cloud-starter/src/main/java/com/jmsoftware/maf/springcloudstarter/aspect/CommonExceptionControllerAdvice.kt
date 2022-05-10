package com.jmsoftware.maf.springcloudstarter.aspect

import cn.hutool.core.util.StrUtil
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.exception.BaseException
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.util.getRequestIpAndPort
import org.apache.catalina.connector.ClientAbortException
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import java.lang.reflect.UndeclaredThrowableException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

/**
 * # CommonExceptionControllerAdvice
 *
 * Exception advice for global controllers.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/13/22 6:06 PM
 */
@RestControllerAdvice
@Order(CommonExceptionControllerAdvice.ORDER)
class CommonExceptionControllerAdvice {
    companion object {
        /**
         * After DatabaseExceptionControllerAdvice
         *
         * @see DatabaseExceptionControllerAdvice
         */
        const val ORDER = 0
        private val log = logger()
    }

    /**
     * Exception handler.
     *
     * **ATTENTION**: In this method, ***cannot throw any exception***.
     *
     * @param request   HTTP request
     * @param exception any kinds of exception occurred in controller
     * @return custom exception info
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleException(request: HttpServletRequest, exception: NoHandlerFoundException): ResponseBodyBean<*> {
        requestLog(request)
        // ATTENTION: Use only ResponseBodyBean.ofStatus() in handleException() method and
        // DON'T throw any exceptions in this method
        log.error("NoHandlerFoundException: Request URL = ${exception.requestURL}, HTTP method = ${exception.httpMethod}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.NOT_FOUND)
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleException(
        request: HttpServletRequest,
        exception: HttpRequestMethodNotSupportedException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("Exception occurred when the request handler does not support a specific request method. Current method is ${exception.method}, Support HTTP method = ${exception.supportedHttpMethods}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(request: HttpServletRequest, exception: MethodArgumentNotValidException): ResponseBodyBean<*> {
        requestLog(request)
        log.error("MethodArgumentNotValidException message: {}", exception.message)
        return ResponseBodyBean.ofStatus<Any>(
            HttpStatus.BAD_REQUEST.value(),
            getFieldErrorMessageFromException(exception),
            null
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleException(
        request: HttpServletRequest,
        exception: MethodArgumentTypeMismatchException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("MethodArgumentTypeMismatchException: Parameter name = ${exception.name}, Exception message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.BAD_REQUEST, removeLineSeparator(exception.message))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleException(request: HttpServletRequest, exception: HttpMessageNotReadableException): ResponseBodyBean<*> {
        requestLog(request)
        log.error("HttpMessageNotReadableException: {}", exception.message)
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.BAD_REQUEST, removeLineSeparator(exception.message))
    }

    @ExceptionHandler(BaseException::class)
    fun handleException(
        request: HttpServletRequest, response: HttpServletResponse,
        exception: BaseException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("BaseException: Status code: ${exception.code}, message: ${exception.message}, data: ${exception.data}")
        response.status = exception.code
        return ResponseBodyBean.ofStatus(
            exception.code,
            removeLineSeparator(exception.message),
            exception.data
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    fun handleException(request: HttpServletRequest, exception: BindException): ResponseBodyBean<*> {
        requestLog(request)
        log.error("BindException message: {} ", exception.message)
        val message: String = if (exception.hasFieldErrors()) {
            val fieldError = exception.fieldError
            "Field [${fieldError?.field}] has error: ${fieldError?.defaultMessage}"
        } else {
            val globalError = exception.globalError
            "Bind error: ${globalError?.defaultMessage}"
        }
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.BAD_REQUEST, message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleException(request: HttpServletRequest, exception: ConstraintViolationException): ResponseBodyBean<*> {
        requestLog(request)
        log.error("ConstraintViolationException message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.BAD_REQUEST, removeLineSeparator(exception.message))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(request: HttpServletRequest, exception: IllegalArgumentException): ResponseBodyBean<*> {
        requestLog(request)
        log.error("IllegalArgumentException message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(
            HttpStatus.BAD_REQUEST.value(),
            removeLineSeparator(exception.message),
            null
        )
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleException(request: HttpServletRequest, exception: BadCredentialsException): ResponseBodyBean<*> {
        requestLog(request)
        // IMPORTANT: org.springframework.security.authentication.BadCredentialsException only exists in the project
        // that depends on org.springframework.boot.spring-boot-starter-security
        log.error("BadCredentialsException message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(
            HttpStatus.FORBIDDEN.value(),
            removeLineSeparator(exception.message),
            null
        )
    }

    @ExceptionHandler(InternalAuthenticationServiceException::class)
    fun handleException(
        request: HttpServletRequest, response: HttpServletResponse,
        exception: InternalAuthenticationServiceException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("An authentication request could not be processed due to a system problem that occurred internally. Exception message: ${exception.message}")
        if (exception.cause is BaseException) {
            val exceptionCause = exception.cause as BaseException
            val code = exceptionCause.code
            response.status = code
            return ResponseBodyBean.ofStatus<Any>(
                HttpStatus.valueOf(code),
                removeLineSeparator(exception.message)
            )
        }
        response.status = HttpStatus.FORBIDDEN.value()
        return ResponseBodyBean.ofStatus<Any>(
            HttpStatus.FORBIDDEN.value(),
            removeLineSeparator(exception.message),
            null
        )
    }

    @ExceptionHandler(ClientAbortException::class)
    fun handleException(request: HttpServletRequest, exception: ClientAbortException) {
        requestLog(request)
        log.error("An abort of a request by a remote client. Exception message: ${exception.message}")
    }

    @ExceptionHandler(UndeclaredThrowableException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleError(exception: UndeclaredThrowableException): ResponseBodyBean<*> {
        log.error("Undeclared throwable exception occurred! Exception message: ${exception.message}", exception)
        return if (exception.cause != null && exception.cause?.message != null) {
            ResponseBodyBean.ofStatus<Any>(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Exception message: ${removeLineSeparator(exception.cause!!.message)}"
            )
        } else ResponseBodyBean.ofStatus<Any>(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Exception message: ${removeLineSeparator(exception.message)}"
        )
    }

    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleError(throwable: Throwable): ResponseBodyBean<*> {
        log.error("Internal server exception occurred! Exception message: ${throwable.message}", throwable)
        return ResponseBodyBean.ofStatus<Any>(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Exception message: ${removeLineSeparator(throwable.message)}"
        )
    }

    private fun requestLog(request: HttpServletRequest) {
        log.error("Exception occurred when [${getRequestIpAndPort(request)}] requested access. Request URL: [${request.method}] ${request.requestURL}")
    }

    /**
     * Get field error message from exception. If two or more fields do not pass Spring Validation check, then will
     * return the 1st error message of the error field.
     *
     * @param exception MethodArgumentNotValidException
     * @return field error message
     */
    private fun getFieldErrorMessageFromException(exception: MethodArgumentNotValidException): String {
        return try {
            val firstErrorField = Objects.requireNonNull(
                exception.bindingResult
                    .allErrors[0]
                    .arguments
            )[0] as DefaultMessageSourceResolvable
            "${firstErrorField.defaultMessage} ${exception.bindingResult.allErrors[0].defaultMessage}"
        } catch (e: Exception) {
            log.error(
                "Exception occurred when get field error message from exception. Exception message: ${e.message}",
                e
            )
            HttpStatus.BAD_REQUEST.reasonPhrase
        }
    }

    /**
     * Remove line separator string.
     *
     * @param source the source
     * @return the string
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/24/2020 11:22 AM
     */
    private fun removeLineSeparator(source: String?): String {
        return if (StrUtil.isBlank(source)) {
            "source is blank"
        } else source!!.replace(System.lineSeparator().toRegex(), " ")
    }
}
