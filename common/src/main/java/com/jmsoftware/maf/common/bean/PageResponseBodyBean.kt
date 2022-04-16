package com.jmsoftware.maf.common.bean

import cn.hutool.json.JSON
import cn.hutool.json.JSONConfig
import cn.hutool.json.JSONUtil
import com.fasterxml.jackson.annotation.JsonFormat
import com.jmsoftware.maf.common.constant.UniversalDateTime
import com.jmsoftware.maf.common.exception.BaseException
import com.jmsoftware.maf.common.exception.InternalServerException
import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime

/**
 * # PageResponseBodyBean
 *
 * Page Response body bean.
 *
 * @param <T> the response body data type
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:02 AM
 */
class PageResponseBodyBean<T> private constructor()  : TrackableBean(), Serializable {
    /**
     * The Timestamp. Must be annotated by '@JsonFormat', otherwise will cause following error, cuz api-gateway does not
     * know how to convert LocalDateTime.
     *
     * Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2021-06-27 23:08:46'
     */
    @JsonFormat(pattern = UniversalDateTime.DATE_TIME_FORMAT)
    val timestamp: LocalDateTime = LocalDateTime.now()

    /**
     * Default status is 200 OK.
     */
    var status = HttpStatus.OK.value()

    /**
     * The Message. Default: 200 OK.
     */
    var message = HttpStatus.OK.reasonPhrase

    /**
     * The List.
     */
    var list = emptyList<T>()

    /**
     * The Total.
     */
    var total: Long = 0

    companion object {
        /**
         * The constant serialVersionUID.
         */
        @Serial
        var serialVersionUID = 4645461634548783641L

        /**
         * Respond to client with IUniversalStatus (status may be OK or other).
         *
         * **ATTENTION:**
         *
         * This method CANNOT be used by controller or service or other class, only provided for Exception controller.
         *
         * @param <T>    the type parameter
         * @param status IUniversalStatus
         * @return response body for ExceptionControllerAdvice javax.servlet.http.HttpServletResponse, Exception)
         */
        fun <T> ofStatus(status: HttpStatus): PageResponseBodyBean<T> {
            val responseBodyBean = PageResponseBodyBean<T>()
            responseBodyBean.status = status.value()
            responseBodyBean.message = status.reasonPhrase
            return responseBodyBean
        }

        /**
         * Of status response body bean.
         *
         * @param <T>     the type parameter
         * @param status  the status
         * @param message the message
         * @return the response body bean
         */
        fun <T> ofStatus(
            status: HttpStatus,
            message: String
        ): PageResponseBodyBean<T> {
            val responseBodyBean = PageResponseBodyBean<T>()
            responseBodyBean.status = status.value()
            responseBodyBean.message = message
            return responseBodyBean
        }

        /**
         * Respond to client with IUniversalStatus and data.
         *
         * **ATTENTION:**
         *
         * This method CANNOT be used by controller or service or other class, only provided for Exception controller
         * .
         *
         * @param <T>    the response body data type
         * @param status IUniversalStatus
         * @param data   data to be responded to client
         * @param total  the total
         * @return response body for ExceptionControllerAdvice
         */
        fun <T> ofStatus(
            status: HttpStatus,
            data: List<T>?,
            total: Long
        ): PageResponseBodyBean<T> {
            val responseBodyBean = PageResponseBodyBean<T>()
            responseBodyBean.status = status.value()
            responseBodyBean.message = status.reasonPhrase
            data?.let { responseBodyBean.list = it }
            responseBodyBean.total = total
            return responseBodyBean
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
         * @param total   the total
         * @return response body for ExceptionControllerAdvice
         */
        fun <T> ofStatus(
            status: Int,
            message: String,
            data: List<T>?,
            total: Long
        ): PageResponseBodyBean<T> {
            val responseBodyBean = PageResponseBodyBean<T>()
            responseBodyBean.status = status
            responseBodyBean.message = message
            data?.let { responseBodyBean.list = it }
            responseBodyBean.total = total
            return responseBodyBean
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
         * @param total   the total
         * @return response body
         * @throws BaseException the base exception
         */
        @Throws(BaseException::class)
        fun <T> setResponse(
            status: Int,
            message: String,
            data: List<T>?,
            total: Long
        ): PageResponseBodyBean<T> {
            if (!HttpStatus.valueOf(status).is2xxSuccessful) {
                throw BaseException(status, message, data)
            }
            val responseBodyBean = PageResponseBodyBean<T>()
            responseBodyBean.status = status
            responseBodyBean.message = message
            data?.let { responseBodyBean.list = it }
            responseBodyBean.total = total
            return responseBodyBean
        }

        /**
         * Respond null data, and status is OK.
         *
         * @param <T> the response body data type
         * @return response body
         */
        fun <T> ofSuccess(): PageResponseBodyBean<T> {
            return PageResponseBodyBean()
        }

        /**
         * Respond data and status is OK.
         *
         * @param <T>   the response body data type
         * @param data  data to be responded to client.
         * @param total the total
         * @return response body
         */
        fun <T> ofSuccess(
            data: List<T>?,
            total: Long
        ): PageResponseBodyBean<T> {
            val responseBodyBean = PageResponseBodyBean<T>()
            data?.let { responseBodyBean.list = it }
            responseBodyBean.total = total
            return responseBodyBean
        }

        /**
         * Respond a message and status is OK.
         *
         * @param <T>     the response body data type
         * @param message message to be responded
         * @return response body
         */
        fun <T> ofSuccess(message: String): PageResponseBodyBean<T> {
            val responseBodyBean = PageResponseBodyBean<T>()
            responseBodyBean.message = message
            return responseBodyBean
        }

        /**
         * Respond data, message and status is OK.
         *
         * @param <T>     the response body data type
         * @param data    data to be responded
         * @param message message to be responded
         * @param total   the total
         * @return response body
         */
        fun <T> ofSuccess(
            data: List<T>?,
            message: String,
            total: Long
        ): PageResponseBodyBean<T> {
            val responseBodyBean = PageResponseBodyBean<T>()
            responseBodyBean.message = message
            data?.let { responseBodyBean.list = it }
            responseBodyBean.total = total
            return responseBodyBean
        }

        /**
         * Respond a message and status is FAILURE(464).
         *
         * @param <T>     the response body data type
         * @param message message to be responded.
         * @return response body
         * @throws InternalServerException the business exception
         */
        fun <T> ofFailure(message: String): PageResponseBodyBean<T> {
            throw InternalServerException(message)
        }

        /**
         * Respond a message and status is FAILURE(464).
         *
         * @param <T>  the response body data type
         * @param data data to be responded
         * @return response body
         * @throws InternalServerException the business exception
         */
        fun <T> ofFailure(data: List<T>?): PageResponseBodyBean<T> {
            throw InternalServerException(data)
        }

        /**
         * Respond data and message, and status if FAILURE(464).
         *
         * @param <T>     the response body data type
         * @param data    data to be responded
         * @param message message to be responded
         * @return response body
         * @throws InternalServerException the business exception
         */
        fun <T> ofFailure(
            data: List<T>?,
            message: String
        ): PageResponseBodyBean<T> {
            throw InternalServerException(data, message)
        }

        /**
         * Respond an ERROR(500).
         *
         * @param <T> the response body data type
         * @return response body
         * @throws BaseException the base exception
         */
        fun <T> ofError(): PageResponseBodyBean<T> {
            return setResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                null,
                0
            )
        }

        /**
         * Respond a custom error.
         *
         * @param <T>    the response body data type
         * @param status Error status, not OK(200)
         * @return response body
         * @throws BaseException the base exception
         */
        fun <T> ofError(status: HttpStatus): PageResponseBodyBean<T> {
            return setResponse(status.value(), status.reasonPhrase, null, 0)
        }

        /**
         * Response an exception.
         *
         * @param <T>       the response body data type
         * @param <B>       Subclass of [BaseException]
         * @param throwable exception
         * @return the response body bean
         * @throws BaseException the base exception
        </B> */
        fun <T, B : BaseException> ofException(throwable: B): PageResponseBodyBean<T> {
            throw throwable
        }

        /**
         * Of json.
         *
         * @param <T>     the type parameter
         * @param message the message
         * @param data    the data
         * @param total   the total
         * @param status  the status
         * @return the json
         * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/22/2020 10:16 AM
         */
        fun <T> of(
            message: String,
            @Nullable data: List<T>?,
            total: Long, status: Int
        ): JSON {
            val responseBodyBean = ofStatus(status, message, data, total)
            val config = JSONConfig()
            config.setIgnoreNullValue(false).dateFormat = "yyyy-MM-dd HH:mm:ss"
            return JSONUtil.parse(responseBodyBean, config)
        }
    }
}
