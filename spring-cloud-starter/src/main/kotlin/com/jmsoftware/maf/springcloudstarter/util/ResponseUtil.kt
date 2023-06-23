package com.jmsoftware.maf.springcloudstarter.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.exception.BaseException
import com.jmsoftware.maf.common.util.logger
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import org.springframework.web.cors.CorsConfiguration
import java.io.IOException

/**
 * # ResponseUtil
 *
 * Response util
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:41 AM
 */
class ResponseUtil(
    private val objectMapper: ObjectMapper
) {
    companion object {
        private val log = logger()
    }

    /**
     * Write data JSON to response.
     *
     * @param response   Response
     * @param httpStatus HTTP status
     * @param data       Data
     */
    fun renderJson(
        response: HttpServletResponse,
        httpStatus: HttpStatus,
        @Nullable data: Any?
    ) {
        standardizeHttpServletResponse(response, httpStatus)
        val responseBodyBean = ResponseBodyBean.ofStatus(
            httpStatus.value(),
            httpStatus.reasonPhrase, data
        )
        try {
            response.writer.write(objectMapper.writeValueAsString(responseBodyBean))
        } catch (e: IOException) {
            log.error("Error occurred when responding a data JSON.", e)
        }
    }

    /**
     * Render json.
     *
     * @param response   the response
     * @param httpStatus the http status
     * @param message    the message
     */
    fun renderJson(
        response: HttpServletResponse,
        httpStatus: HttpStatus,
        message: String
    ) {
        standardizeHttpServletResponse(response, httpStatus)
        val responseBodyBean = ResponseBodyBean.ofStatus<Any>(httpStatus.value(), message, null)
        try {
            response.writer.write(objectMapper.writeValueAsString(responseBodyBean))
        } catch (e: IOException) {
            log.error("Error occurred when responding a data JSON.", e)
        }
    }

    /**
     * Write exception JSON to response.
     *
     * @param response  Response
     * @param exception Exception
     */
    fun renderJson(response: HttpServletResponse, exception: BaseException) {
        val httpStatus = HttpStatus.valueOf(exception.code)
        standardizeHttpServletResponse(response, httpStatus)
        val responseBodyBean = ResponseBodyBean.ofStatus(
            exception.code,
            exception.message,
            exception.data
        )
        try {
            response.writer.write(objectMapper.writeValueAsString(responseBodyBean))
        } catch (e: IOException) {
            log.error("Error occurred when responding an exception JSON.", e)
        }
    }

    /**
     * Standardize http servlet response.
     *
     * @param response   the response
     * @param httpStatus the http status
     */
    private fun standardizeHttpServletResponse(response: HttpServletResponse, httpStatus: HttpStatus) {
        response.setHeader("Access-Control-Allow-Origin", CorsConfiguration.ALL)
        response.setHeader("Access-Control-Allow-Methods", CorsConfiguration.ALL)
        response.contentType = "application/json;charset=UTF-8"
        response.status = httpStatus.value()
    }
}
