package com.jmsoftware.maf.springcloudstarter.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>DatabaseExceptionControllerAdvice</h1>
 * <p>
 * Exception advice for database exception.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 10:41 AM
 **/
@Slf4j
@RestControllerAdvice
@Order(DatabaseExceptionControllerAdvice.ORDER)
@ConditionalOnClass({MyBatisSystemException.class, MybatisPlusException.class, PersistenceException.class})
public class DatabaseExceptionControllerAdvice {
    /**
     * Before CommonExceptionControllerAdvice
     *
     * @see CommonExceptionControllerAdvice
     */
    public static final int ORDER = -1;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MyBatisSystemException.class)
    public ResponseBodyBean<?> handleMyBatisSystemException(HttpServletRequest request,
                                                            MyBatisSystemException exception) {
        this.requestLog(request);
        log.error("MyBatisSystemException message: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("MyBatisSystemException message: %s",
                                                       this.removeLineSeparator(exception.getMessage())));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MybatisPlusException.class)
    public ResponseBodyBean<?> handleMybatisPlusException(HttpServletRequest request, MybatisPlusException exception) {
        this.requestLog(request);
        log.error("MybatisPlusException message: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("MybatisPlusException message: %s",
                                                       this.removeLineSeparator(exception.getMessage())));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PersistenceException.class)
    public ResponseBodyBean<?> handlePersistenceException(HttpServletRequest request, PersistenceException exception) {
        this.requestLog(request);
        log.error("PersistenceException message: {}", exception.getMessage());
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("PersistenceException message: %s",
                                                       this.removeLineSeparator(exception.getMessage())));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseBodyBean<?> handleBadSqlGrammarException(HttpServletRequest request,
                                                            BadSqlGrammarException exception) {
        this.requestLog(request);
        log.error("BadSqlGrammarException message: {}", exception.getMessage());
        var message = exception.getMessage();
        if (ObjectUtil.isNotNull(exception.getCause()) && StrUtil.isNotBlank(exception.getCause().getMessage())) {
            message = exception.getCause().getMessage();
        }
        return ResponseBodyBean.ofStatus(HttpStatus.INTERNAL_SERVER_ERROR,
                                         String.format("PersistenceException message: %s",
                                                       this.removeLineSeparator(message)));
    }

    private void requestLog(HttpServletRequest request) {
        log.error("Exception occurred when [{}] requested access. Request URL: [{}] {}",
                  RequestUtil.getRequestIpAndPort(request), request.getMethod(), request.getRequestURL());
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
