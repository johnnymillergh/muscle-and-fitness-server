package com.jmsoftware.maf.common.bean

import cn.hutool.json.JSON
import cn.hutool.json.JSONConfig
import cn.hutool.json.JSONUtil
import com.fasterxml.jackson.annotation.JsonFormat
import com.jmsoftware.maf.common.constant.ISO_8601_DATETIME_TIMEZONE_FORMAT
import com.jmsoftware.maf.common.exception.BaseException
import com.jmsoftware.maf.common.exception.InternalServerException
import org.springframework.http.HttpStatus
import java.io.Serial
import java.io.Serializable
import java.time.OffsetDateTime

/**
 * # ResponseBodyBean
 *
 * Response body bean.
 *
 * @param <T> the response body data type
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:23 AM
 */
data class ResponseBodyBean<T> constructor(
    /**
     * The Timestamp. Must be annotated by '@JsonFormat', otherwise will cause following error, cuz api-gateway does not
     * know how to convert LocalDateTime.
     *
     *
     * Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2021-06-27
     * 23:08:46'
     */
    @JsonFormat(pattern = ISO_8601_DATETIME_TIMEZONE_FORMAT)
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    /**
     * Default status is 200 OK.
     */
    val status: Int = HttpStatus.OK.value(),
    /**
     * The Message. Default: 200 OK.
     */
    val message: String = HttpStatus.OK.reasonPhrase,
    /**
     * The Data.
     */
    val data: T? = null
) : TrackableBean(), Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 4645469240048361965L

        /**
         * Respond to client with IUniversalStatus (status may be OK or other).
         *
         * **ATTENTION:**
         *
         * This method CANNOT be used by controller or service or other class, only provided for Exception controller
         * .
         *
         * @param <T>    the type parameter
         * @param status IUniversalStatus
         * @return response body for ExceptionControllerAdvice javax.servlet.http.HttpServletResponse, Exception)
         */
        fun <T> ofStatus(status: HttpStatus): ResponseBodyBean<T> {
            return ResponseBodyBean(status = status.value(), message = status.reasonPhrase)
        }

        /**
         * Of status response body bean.
         *
         * @param <T>     the type parameter
         * @param status  the status
         * @param message the message
         * @return the response body bean
         */
        fun <T> ofStatus(status: HttpStatus, message: String): ResponseBodyBean<T> {
            return ResponseBodyBean(status = status.value(), message = message)
        }

        /**
         * Respond to client with IUniversalStatus and data.
         *
         * **ATTENTION:**
         *
         * This method CANNOT be used by controller or service or other class, only provided for Exception controller
         *
         * @param <T>    the response body data type
         * @param status IUniversalStatus
         * @param data   data to be responded to client
         * @return response body for ExceptionControllerAdvice
         */
        fun <T> ofStatus(status: HttpStatus, data: T?): ResponseBodyBean<T> {
            return ResponseBodyBean(status = status.value(), message = status.reasonPhrase, data = data)
        }

        /**
         * Highly customizable response. Status might be any HttpStatus&#39; code value.
         *
         * **ATTENTION:**
         *
         * This method CANNOT be used by controller or service or other class, only provided for Exception controller.
         *
         * @param <T>     the response body data type
         * @param status  status code
         * @param message message to be responded
         * @param data    data to be responded
         * @return response body for ExceptionControllerAdvice
         */
        fun <T> ofStatus(
            status: Int,
            message: String,
            data: T?
        ): ResponseBodyBean<T> {
            return ResponseBodyBean(status = status, message = message, data = data)
        }

        /**
         * Highly customizable response. Status might be any HttpStatus&#39; code value.
         *
         * **ATTENTION:**
         *
         * This method CANNOT be used in ExceptionControllerAdvice.
         *
         * @param <T>     the response body data type
         * @param status  status code
         * @param message message to be responded
         * @param data    data to be responded
         * @return response body
         */
        fun <T> setResponse(
            status: Int,
            message: String,
            data: T?
        ): ResponseBodyBean<T> {
            if (!HttpStatus.valueOf(status).is2xxSuccessful) {
                throw BaseException(message, status, data)
            }
            return ResponseBodyBean(status = status, message = message, data = data)
        }

        /**
         * Respond null data, and status is OK.
         *
         * @param <T> the response body data type
         * @return response body
         */
        fun <T> ofSuccess(): ResponseBodyBean<T> {
            return ResponseBodyBean()
        }

        /**
         * Respond data and status is OK.
         *
         * @param <T>  the response body data type
         * @param data data to be responded to client.
         * @return response body
         */
        fun <T> ofSuccess(data: T?): ResponseBodyBean<T> {
            return ResponseBodyBean(data = data)
        }

        /**
         * Respond a message and status is OK.
         *
         * @param <T>     the response body data type
         * @param message message to be responded
         * @return response body
         */
        fun <T> ofSuccessMessage(message: String): ResponseBodyBean<T> {
            return ResponseBodyBean(message = message)
        }

        /**
         * Respond data, message and status is OK.
         *
         * @param <T>     the response body data type
         * @param data    data to be responded
         * @param message message to be responded
         * @return response body
         */
        fun <T> ofSuccess(
            data: T?,
            message: String
        ): ResponseBodyBean<T> {
            return ResponseBodyBean(message = message, data = data)
        }

        /**
         * Respond a message and status is FAILURE(464).
         *
         * @param <T>     the response body data type
         * @param message message to be responded.
         * @return response body
         */
        fun <T> ofFailureMessage(message: String): ResponseBodyBean<T> {
            throw InternalServerException(message)
        }

        /**
         * Respond a message and status is FAILURE(464).
         *
         * @param <T>  the response body data type
         * @param data data to be responded
         * @return response body
         */
        fun <T> ofFailure(data: T): ResponseBodyBean<T> {
            throw InternalServerException(data)
        }

        /**
         * Respond data and message, and status if FAILURE(464).
         *
         * @param <T>     the response body data type
         * @param data    data to be responded
         * @param message message to be responded
         * @return response body
         */
        fun <T> ofFailure(data: T, message: String): ResponseBodyBean<T> {
            throw InternalServerException(message = message, data = data)
        }

        /**
         * Respond an ERROR(500).
         *
         * @param <T> the response body data type
         * @return response body
         */
        fun <T> ofError(): ResponseBodyBean<T> {
            return setResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                null
            )
        }

        /**
         * Respond a custom error.
         *
         * @param <T>    the response body data type
         * @param status Error status, not OK(200)
         * @return response body
         */
        fun <T> ofError(status: HttpStatus): ResponseBodyBean<T> {
            return setResponse(status.value(), status.reasonPhrase, null)
        }

        /**
         * Response an exception.
         *
         * @param <T>       the response body data type
         * @param <B>       Sub-class of [BaseException]
         * @param throwable exception
         * @return the response body bean
         */
        fun <T, B : BaseException> ofException(throwable: B): ResponseBodyBean<T> {
            throw throwable
        }

        /**
         * Of json.
         *
         * @param message the message
         * @param data    the data
         * @param status  the status
         * @return the json
         * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/22/2020 10:16 AM
         */
        fun of(message: String, data: Any?, status: Int): JSON {
            val responseBodyBean = ofStatus(status, message, data)
            val config = JSONConfig()
            config.setIgnoreNullValue(false).dateFormat = ISO_8601_DATETIME_TIMEZONE_FORMAT
            return JSONUtil.parse(responseBodyBean, config)
        }
    }
}
