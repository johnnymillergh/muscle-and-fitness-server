package com.jmsoftware.authcenter.universal.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.exception.BaseException;
import com.jmsoftware.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
            log.error("[GlobalExceptionCapture] NoHandlerFoundException: Request URL = {}, HTTP method = {}",
                      ((NoHandlerFoundException) exception).getRequestURL(),
                      ((NoHandlerFoundException) exception).getHttpMethod());
            response.setStatus(HttpStatus.NOT_FOUND.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.NOT_FOUND);
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            log.error("[GlobalExceptionCapture] HttpRequestMethodNotSupportedException: " +
                      "Current method is {}, Support HTTP method = {}",
                      ((HttpRequestMethodNotSupportedException) exception).getMethod(),
                      JSONUtil.toJsonStr(
                              ((HttpRequestMethodNotSupportedException) exception).getSupportedHttpMethods()));
            response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.METHOD_NOT_ALLOWED);
        } else if (exception instanceof MethodArgumentNotValidException) {
            log.error("[GlobalExceptionCapture] MethodArgumentNotValidException: {}", exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.getCode(),
                                             getFieldErrorMessageFromException((MethodArgumentNotValidException) exception),
                                             null);
        } else if (exception instanceof ConstraintViolationException) {
            log.error("[GlobalExceptionCapture] ConstraintViolationException: {}", exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.BAD_REQUEST.getCode(),
                                             CollUtil.getFirst(((ConstraintViolationException) exception)
                                                                       .getConstraintViolations())
                                                     .getMessage(), null);
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            log.error("[GlobalExceptionCapture] MethodArgumentTypeMismatchException: " +
                      "Parameter name = {}, Exception information: {}",
                      ((MethodArgumentTypeMismatchException) exception).getName(),
                      ((MethodArgumentTypeMismatchException) exception).getMessage());
            response.setStatus(HttpStatus.PARAM_NOT_MATCH.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.PARAM_NOT_MATCH);
        } else if (exception instanceof HttpMessageNotReadableException) {
            log.error("[GlobalExceptionCapture] HttpMessageNotReadableException: {}",
                      ((HttpMessageNotReadableException) exception).getMessage());
            response.setStatus(HttpStatus.PARAM_NOT_NULL.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.PARAM_NOT_NULL);
        } else if (exception instanceof BaseException) {
            log.error("[GlobalExceptionCapture] BaseException: Status code: {}, message: {}, data: {}",
                      ((BaseException) exception).getCode(),
                      exception.getMessage(),
                      ((BaseException) exception).getData());
            response.setStatus(((BaseException) exception).getCode());
            return ResponseBodyBean.ofStatus(((BaseException) exception).getCode(),
                                             exception.getMessage(),
                                             ((BaseException) exception).getData());
        } else if (exception instanceof BindException) {
            log.error("[GlobalExceptionCapture]: Exception information: {} ", exception.getMessage());
            response.setStatus(HttpStatus.INVALID_PARAM.getCode());
            return ResponseBodyBean.ofStatus(HttpStatus.INVALID_PARAM);
        }
        log.error("[GlobalExceptionCapture]: Exception information: {} ", exception.getMessage(), exception);
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
            DefaultMessageSourceResolvable firstErrorField =
                    (DefaultMessageSourceResolvable) Objects.requireNonNull(exception.getBindingResult()
                                                                                    .getAllErrors()
                                                                                    .get(0)
                                                                                    .getArguments())[0];
            String firstErrorFieldName = firstErrorField.getDefaultMessage();
            String firstErrorFieldMessage = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
            return firstErrorFieldName + " " + firstErrorFieldMessage;
        } catch (Exception e) {
            log.error("Exception occurred when get field error message from exception. Exception message: {}",
                      e.getMessage(),
                      e);
            return HttpStatus.INVALID_PARAM.getMessage();
        }
    }
}
