package com.jmsoftware.maf.springcloudstarter.aspect;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <h1>DatabaseExceptionControllerAdvice</h1>
 * <p>
 * Exception advice for database exception.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 10:41 AM
 **/
@Slf4j
@RestControllerAdvice
@ConditionalOnClass({MyBatisSystemException.class, MybatisPlusException.class, PersistenceException.class})
public class DatabaseExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MyBatisSystemException.class)
    public ResponseBodyBean<?> handleMyBatisSystemException(HttpServletRequest request,
                                                            MyBatisSystemException exception) {
        requestLog(request);
        log.error("MyBatisSystemException message: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("MyBatisSystemException message: %s",
                                                       removeLineSeparator(exception.getMessage())));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MybatisPlusException.class)
    public ResponseBodyBean<?> handleMybatisPlusException(HttpServletRequest request, MybatisPlusException exception) {
        requestLog(request);
        log.error("MybatisPlusException message: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("MybatisPlusException message: %s",
                                                       removeLineSeparator(exception.getMessage())));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PersistenceException.class)
    public ResponseBodyBean<?> handlePersistenceException(HttpServletRequest request, PersistenceException exception) {
        requestLog(request);
        log.error("PersistenceException message: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("PersistenceException message: %s",
                                                       removeLineSeparator(exception.getMessage())));
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
