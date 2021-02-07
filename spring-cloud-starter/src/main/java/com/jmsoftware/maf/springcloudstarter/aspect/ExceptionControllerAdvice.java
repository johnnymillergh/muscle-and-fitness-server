package com.jmsoftware.maf.springcloudstarter.aspect;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BaseException;
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * <h1>ExceptionControllerAdvice</h1>
 * <p>
 * Exception advice for global controllers.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:39
 **/
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    /**
     * <p>Exception handler.</p>
     * <p><strong>ATTENTION</strong>: In this method, <strong><em>cannot throw any exception</em></strong>.</p>
     *
     * @param request   HTTP request
     * @param exception any kinds of exception occurred in controller
     * @return custom exception info
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, NoHandlerFoundException exception) {
        requestLog(request);
        //  ATTENTION: Use only ResponseBodyBean.ofStatus() in handleException() method and
        //  DON'T throw any exceptions in this method
        log.error("NoHandlerFoundException: Request URL = {}, HTTP method = {}", exception.getRequestURL(),
                  exception.getHttpMethod());
        return ResponseBodyBean.ofStatus(HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request,
                                               HttpRequestMethodNotSupportedException exception) {
        requestLog(request);
        log.error("Exception occurred when the request handler does not support a specific request method. " +
                          "Current method is {}, Support HTTP method = {}", exception.getMethod(),
                  exception.getSupportedHttpMethods());
        return ResponseBodyBean.ofStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        requestLog(request);
        log.error("Exception occurred when validation on an argument annotated with fails. Exception message: {}",
                  exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.value(), getFieldErrorMessageFromException(exception),
                                         null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request,
                                               MethodArgumentTypeMismatchException exception) {
        requestLog(request);
        log.error("MethodArgumentTypeMismatchException: Parameter name = {}, Exception message: {}",
                  exception.getName(), exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST, removeLineSeparator(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        requestLog(request);
        log.error("HttpMessageNotReadableException: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST, removeLineSeparator(exception.getMessage()));
    }

    @ExceptionHandler(value = BaseException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, HttpServletResponse response,
                                               BaseException exception) {
        requestLog(request);
        log.error("BaseException: Status code: {}, message: {}, data: {}", exception.getCode(),
                  exception.getMessage(), exception.getData());
        response.setStatus(exception.getCode());
        return ResponseBodyBean.ofStatus(exception.getCode(), removeLineSeparator(exception.getMessage()),
                                         exception.getData());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, BindException exception) {
        requestLog(request);
        log.error("Exception message: {} ", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST, removeLineSeparator(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, IllegalArgumentException exception) {
        requestLog(request);
        log.error("Exception message: {} ", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.value(), removeLineSeparator(exception.getMessage()),
                                         null);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, BadCredentialsException exception) {
        requestLog(request);
        // IMPORTANT: org.springframework.security.authentication.BadCredentialsException only exists in the project
        // that depends on org.springframework.boot.spring-boot-starter-security
        log.error("Exception message: {} ", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.FORBIDDEN.value(), removeLineSeparator(exception.getMessage()),
                                         null);
    }

    @ExceptionHandler(value = InternalAuthenticationServiceException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, HttpServletResponse response,
                                               InternalAuthenticationServiceException exception) {
        requestLog(request);
        log.error("An authentication request could not be processed due to a system problem that occurred " +
                          "internally. Exception message: {} ", exception.getMessage());
        if (exception.getCause() instanceof BaseException) {
            val exceptionCause = (BaseException) exception.getCause();
            val code = exceptionCause.getCode();
            response.setStatus(code);
            return ResponseBodyBean.ofStatus(HttpStatus.valueOf(code), removeLineSeparator(exception.getMessage()));
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return ResponseBodyBean.ofStatus(HttpStatus.FORBIDDEN.value(), removeLineSeparator(exception.getMessage()),
                                         null);
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBodyBean<?> handleError(Throwable ex) {
        log.error("Internal server exception occurred! Exception message: {} ", ex.getMessage(), ex);
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("Exception message: %s", removeLineSeparator(ex.getMessage())));
    }

    private void requestLog(HttpServletRequest request) {
        log.error("Exception occurred when [{}] requested access. Request URL: [{}] {}",
                  RequestUtil.getRequestIpAndPort(request), request.getMethod(), request.getRequestURL());
    }

    /**
     * Get field error message from exception. If two or more fields do not pass Spring Validation check, then will
     * return the 1st error message of the error field.
     *
     * @param exception MethodArgumentNotValidException
     * @return field error message
     */
    private String getFieldErrorMessageFromException(MethodArgumentNotValidException exception) {
        try {
            val firstErrorField =
                    (DefaultMessageSourceResolvable) Objects.requireNonNull(exception.getBindingResult()
                                                                                    .getAllErrors()
                                                                                    .get(0)
                                                                                    .getArguments())[0];
            val firstErrorFieldName = firstErrorField.getDefaultMessage();
            val firstErrorFieldMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            return String.format("%s %s", firstErrorFieldName, firstErrorFieldMessage);
        } catch (Exception e) {
            log.error("Exception occurred when get field error message from exception. Exception message: {}",
                      e.getMessage(), e);
            return HttpStatus.BAD_REQUEST.getReasonPhrase();
        }
    }

    /**
     * Remove line separator string.
     *
     * @param source the source
     * @return the string
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/24/2020 11:22 AM
     */
    private String removeLineSeparator(String source) {
        if (StrUtil.isBlank(source)) {
            return "source is blank";
        }
        return source.replaceAll(System.lineSeparator(), " ");
    }
}
