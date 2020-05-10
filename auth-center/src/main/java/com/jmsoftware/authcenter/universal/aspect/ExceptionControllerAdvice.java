package com.jmsoftware.authcenter.universal.aspect;

import cn.hutool.core.collection.CollUtil;
import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.BaseException;
import com.jmsoftware.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * <h1>ExceptionControllerAdvice</h1>
 * <p>
 * Exception advice for global controllers.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:39
 **/
@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {
    /**
     * <p>Exception handler.</p>
     * <p><strong>ATTENTION</strong>: In this method, <strong><em>cannot throw any exception</em></strong>.</p>
     *
     * @param request   HTTP request
     * @param exception any kinds of exception occurred in controller
     * @return custom exception info
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseBodyBean<Object> handleException(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    Exception exception) {
        log.error("Exception occurred when [{}] requested access. URL: {}",
                  RequestUtil.getRequestIpAndPort(request),
                  request.getServletPath());

        // FIXME: THIS IS NOT A PROBLEM
        //  ATTENTION: Use only ResponseBodyBean.ofStatus() in handleException() method and DON'T throw any exception
        if (exception instanceof NoHandlerFoundException) {
            log.error("NoHandlerFoundException: Request URL = {}, HTTP method = {}",
                      ((NoHandlerFoundException) exception).getRequestURL(),
                      ((NoHandlerFoundException) exception).getHttpMethod());
            response.setStatus(HttpStatus.NOT_FOUND.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.NOT_FOUND);
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            log.error("Exception occurred when the request handler does not support a specific request method. " +
                      "Current method is {}, Support HTTP method = {}",
                      ((HttpRequestMethodNotSupportedException) exception).getMethod(),
                      ((HttpRequestMethodNotSupportedException) exception).getSupportedHttpMethods());
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.METHOD_NOT_ALLOWED);
        } else if (exception instanceof MethodArgumentNotValidException) {
            log.error("Exception occurred when validation on an argument annotated with fails. Exception message: {}",
                      exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.getCode(),
                                             getFieldErrorMessageFromException((MethodArgumentNotValidException) exception),
                                             null);
        } else if (exception instanceof ConstraintViolationException) {
            log.error("Constraint violations exception occurred. Exception message: {}", exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.getCode(),
                                             CollUtil.getFirst(((ConstraintViolationException) exception).getConstraintViolations()).getMessage(),
                                             null);
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            log.error("MethodArgumentTypeMismatchException: Parameter name = {}, Exception message: {}",
                      ((MethodArgumentTypeMismatchException) exception).getName(),
                      ((MethodArgumentTypeMismatchException) exception).getMessage());
            response.setStatus(HttpStatus.PARAM_NOT_MATCH.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.PARAM_NOT_MATCH);
        } else if (exception instanceof HttpMessageNotReadableException) {
            log.error("HttpMessageNotReadableException: {}",
                      ((HttpMessageNotReadableException) exception).getMessage());
            response.setStatus(HttpStatus.PARAM_NOT_NULL.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.PARAM_NOT_NULL);
        } else if (exception instanceof BaseException) {
            log.error("BaseException: Status code: {}, message: {}, data: {}", ((BaseException) exception).getCode(),
                      exception.getMessage(), ((BaseException) exception).getData());
            response.setStatus(((BaseException) exception).getCode());
            return ResponseBodyBean.ofStatus(((BaseException) exception).getCode(), exception.getMessage(),
                                             ((BaseException) exception).getData());
        } else if (exception instanceof BindException) {
            log.error("Exception message: {} ", exception.getMessage());
            response.setStatus(HttpStatus.INVALID_PARAM.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.INVALID_PARAM);
        } else if (exception instanceof IllegalArgumentException) {
            log.error("Exception message: {} ", exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.getCode(), exception.getMessage(), null);
        } else if (exception instanceof BadCredentialsException) {
            // IMPORTANT: org.springframework.security.authentication.BadCredentialsException only exists in the project
            // that depends on org.springframework.boot.spring-boot-starter-security
            log.error("Exception message: {} ", exception.getMessage());
            response.setStatus(HttpStatus.BAD_CREDENTIALS.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.BAD_CREDENTIALS.getCode(), exception.getMessage(), null);
        } else if (exception instanceof InternalAuthenticationServiceException) {
            log.error("An authentication request could not be processed due to a system problem that occurred " +
                      "internally. Exception message: {} ", exception.getMessage());
            if (exception.getCause() instanceof BaseException) {
                val exceptionCause = (BaseException) exception.getCause();
                val code = exceptionCause.getCode();
                response.setStatus(code);
                return ResponseBodyBean.ofStatus(HttpStatus.fromCode(code));
            }
            response.setStatus(HttpStatus.BAD_CREDENTIALS.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.BAD_CREDENTIALS.getCode(), exception.getMessage(), null);
        }
        log.error("Internal system exception occurred! Exception message: {} ", exception.getMessage(), exception);
        response.setStatus(HttpStatus.ERROR.getCode());
        return ResponseBodyBean.ofStatus(HttpStatus.ERROR.getCode(), HttpStatus.ERROR.getMessage(), null);
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
            return HttpStatus.INVALID_PARAM.getMessage();
        }
    }
}
