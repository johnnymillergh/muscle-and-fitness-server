package com.jmsoftware.maf.springcloudstarter.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BaseException;
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
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
import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
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
@Order(0)
@RestControllerAdvice
public class CommonExceptionControllerAdvice {
    /**
     * <p>Exception handler.</p>
     * <p><strong>ATTENTION</strong>: In this method, <strong><em>cannot throw any exception</em></strong>.</p>
     *
     * @param request   HTTP request
     * @param exception any kinds of exception occurred in controller
     * @return custom exception info
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, NoHandlerFoundException exception) {
        this.requestLog(request);
        //  ATTENTION: Use only ResponseBodyBean.ofStatus() in handleException() method and
        //  DON'T throw any exceptions in this method
        log.error("NoHandlerFoundException: Request URL = {}, HTTP method = {}", exception.getRequestURL(),
                  exception.getHttpMethod());
        return ResponseBodyBean.ofStatus(HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request,
                                               HttpRequestMethodNotSupportedException exception) {
        this.requestLog(request);
        log.error("Exception occurred when the request handler does not support a specific request method. " +
                          "Current method is {}, Support HTTP method = {}", exception.getMethod(),
                  exception.getSupportedHttpMethods());
        return ResponseBodyBean.ofStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        this.requestLog(request);
        log.error("MethodArgumentNotValidException message: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.value(), this.getFieldErrorMessageFromException(exception),
                                         null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request,
                                               MethodArgumentTypeMismatchException exception) {
        this.requestLog(request);
        log.error("MethodArgumentTypeMismatchException: Parameter name = {}, Exception message: {}",
                  exception.getName(), exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST, this.removeLineSeparator(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        this.requestLog(request);
        log.error("HttpMessageNotReadableException: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST, this.removeLineSeparator(exception.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, HttpServletResponse response,
                                               BaseException exception) {
        this.requestLog(request);
        log.error("BaseException: Status code: {}, message: {}, data: {}", exception.getCode(),
                  exception.getMessage(), exception.getData());
        response.setStatus(exception.getCode());
        return ResponseBodyBean.ofStatus(exception.getCode(), this.removeLineSeparator(exception.getMessage()),
                                         exception.getData());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, BindException exception) {
        this.requestLog(request);
        log.error("BindException message: {} ", exception.getMessage());
        String message;
        if (exception.hasFieldErrors()) {
            val fieldError = exception.getFieldError();
            message = String.format("Field [%s] has error: %s", Objects.requireNonNull(fieldError).getField(),
                                    fieldError.getDefaultMessage());
        } else {
            val globalError = exception.getGlobalError();
            message = String.format("Bind error: %s", Objects.requireNonNull(globalError).getDefaultMessage());
        }
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST, message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, ConstraintViolationException exception) {
        this.requestLog(request);
        log.error("ConstraintViolationException message: {} ", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST, this.removeLineSeparator(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, IllegalArgumentException exception) {
        this.requestLog(request);
        log.error("IllegalArgumentException message: {} ", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.value(), this.removeLineSeparator(exception.getMessage()),
                                         null);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, BadCredentialsException exception) {
        this.requestLog(request);
        // IMPORTANT: org.springframework.security.authentication.BadCredentialsException only exists in the project
        // that depends on org.springframework.boot.spring-boot-starter-security
        log.error("BadCredentialsException message: {} ", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.FORBIDDEN.value(), this.removeLineSeparator(exception.getMessage()),
                                         null);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseBodyBean<?> handleException(HttpServletRequest request, HttpServletResponse response,
                                               InternalAuthenticationServiceException exception) {
        this.requestLog(request);
        log.error("An authentication request could not be processed due to a system problem that occurred " +
                          "internally. Exception message: {} ", exception.getMessage());
        if (exception.getCause() instanceof BaseException) {
            val exceptionCause = (BaseException) exception.getCause();
            val code = exceptionCause.getCode();
            response.setStatus(code);
            return ResponseBodyBean.ofStatus(HttpStatus.valueOf(code),
                                             this.removeLineSeparator(exception.getMessage()));
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return ResponseBodyBean.ofStatus(HttpStatus.FORBIDDEN.value(), this.removeLineSeparator(exception.getMessage()),
                                         null);
    }

    @ExceptionHandler(ClientAbortException.class)
    public void handleException(HttpServletRequest request, ClientAbortException exception) {
        this.requestLog(request);
        log.error("An abort of a request by a remote client. Exception message: {} ", exception.getMessage());
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBodyBean<?> handleError(UndeclaredThrowableException exception) {
        log.error("Undeclared throwable exception occurred! Exception message: {} ", exception.getMessage(), exception);
        if (ObjectUtil.isNotNull(exception.getCause()) && StrUtil.isNotEmpty(exception.getCause().getMessage())) {
            return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                             String.format("Exception message: %s",
                                                           this.removeLineSeparator(
                                                                   exception.getCause().getMessage())));
        }
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("Exception message: %s",
                                                       this.removeLineSeparator(exception.getMessage())));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseBodyBean<?> handleError(Throwable ex) {
        log.error("Internal server exception occurred! Exception message: {} ", ex.getMessage(), ex);
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("Exception message: %s", this.removeLineSeparator(ex.getMessage())));
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
