package com.jmsoftware.maf.springcloudstarter.aspect

import cn.hutool.core.exceptions.ExceptionUtil.getRootCauseMessage
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.util.getRequestIpAndPort
import jakarta.servlet.http.HttpServletRequest
import org.apache.ibatis.exceptions.PersistenceException
import org.mybatis.spring.MyBatisSystemException
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * # DatabaseExceptionControllerAdvice
 *
 * Exception advice for database exception.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/18/22 9:20 PM
 */
@RestControllerAdvice
@ConditionalOnClass(
    MyBatisSystemException::class,
    MybatisPlusException::class,
    PersistenceException::class
)
@Order(DatabaseExceptionControllerAdvice.ORDER)
class DatabaseExceptionControllerAdvice {
    companion object {
        /**
         * Before CommonExceptionControllerAdvice
         *
         * @see CommonExceptionControllerAdvice
         */
        const val ORDER = -1
        private val log = logger()
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MyBatisSystemException::class)
    fun handleMyBatisSystemException(
        request: HttpServletRequest,
        exception: MyBatisSystemException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("MyBatisSystemException message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.INTERNAL_SERVER_ERROR, getRootCauseMessage(exception))
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MybatisPlusException::class)
    fun handleMybatisPlusException(
        request: HttpServletRequest,
        exception: MybatisPlusException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("MybatisPlusException message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.INTERNAL_SERVER_ERROR, getRootCauseMessage(exception))
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(PersistenceException::class)
    fun handlePersistenceException(
        request: HttpServletRequest,
        exception: PersistenceException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("PersistenceException message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.INTERNAL_SERVER_ERROR, getRootCauseMessage(exception))
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadSqlGrammarException::class)
    fun handleBadSqlGrammarException(
        request: HttpServletRequest,
        exception: BadSqlGrammarException
    ): ResponseBodyBean<*> {
        requestLog(request)
        log.error("BadSqlGrammarException message: ${exception.message}")
        return ResponseBodyBean.ofStatus<Any>(HttpStatus.INTERNAL_SERVER_ERROR, getRootCauseMessage(exception))
    }

    private fun requestLog(request: HttpServletRequest) {
        log.error("Exception occurred when [${getRequestIpAndPort(request)}] requested access. Request URL: [${request.method}] ${request.requestURL}")
    }
}
