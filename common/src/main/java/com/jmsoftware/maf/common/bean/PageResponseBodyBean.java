package com.jmsoftware.maf.common.bean;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jmsoftware.maf.common.constant.UniversalDateTime;
import com.jmsoftware.maf.common.exception.BaseException;
import com.jmsoftware.maf.common.exception.InternalServerException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <h1>PageResponseBodyBean</h1>
 * <p>
 * Page Response body bean.
 *
 * @param <T> the response body data type
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 4:24 PM
 */
@Data
@SuppressWarnings("unused")
public class PageResponseBodyBean<T> implements Serializable {
    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 4645461634548783641L;

    /**
     * The Timestamp. Must be annotated by '@JsonFormat', otherwise will cause following error, cuz api-gateway does not
     * know how to convert LocalDateTime.
     * <p>
     * Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2021-06-27
     * 23:08:46'
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern = UniversalDateTime.DATE_TIME_FORMAT)
    final LocalDateTime timestamp = LocalDateTime.now();
    /**
     * Default status is 200 OK.
     */
    private Integer status = HttpStatus.OK.value();
    /**
     * The Message. Default: 200 OK.
     */
    private String message = HttpStatus.OK.getReasonPhrase();
    /**
     * The List.
     */
    private List<T> list = Collections.emptyList();
    /**
     * The Total.
     */
    private long total;

    /**
     * <p>Respond to client with IUniversalStatus (status may be OK or other).</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param <T>    the type parameter
     * @param status IUniversalStatus
     * @return response body for ExceptionControllerAdvice javax.servlet.http.HttpServletResponse, Exception)
     */
    public static <T> PageResponseBodyBean<T> ofStatus(@NonNull final HttpStatus status) {
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setStatus(status.value());
        responseBodyBean.setMessage(status.getReasonPhrase());
        return responseBodyBean;
    }

    /**
     * Of status response body bean.
     *
     * @param <T>     the type parameter
     * @param status  the status
     * @param message the message
     * @return the response body bean
     */
    public static <T> PageResponseBodyBean<T> ofStatus(@NonNull final HttpStatus status,
                                                       @NonNull final String message) {
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setStatus(status.value());
        responseBodyBean.setMessage(message);
        return responseBodyBean;
    }

    /**
     * <p>Respond to client with IUniversalStatus and data.</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param <T>    the response body data type
     * @param status IUniversalStatus
     * @param data   data to be responded to client
     * @param total  the total
     * @return response body for ExceptionControllerAdvice
     */
    public static <T> PageResponseBodyBean<T> ofStatus(@NonNull final HttpStatus status,
                                                       final List<T> data,
                                                       final long total) {
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setStatus(status.value());
        responseBodyBean.setMessage(status.getReasonPhrase());
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * <p>Highly customizable response. Status might be any HttpStatus&#39; code value.</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param <T>     the response body data type
     * @param status  status code
     * @param message message to be responded
     * @param data    data to be responded
     * @param total   the total
     * @return response body for ExceptionControllerAdvice
     */
    public static <T> PageResponseBodyBean<T> ofStatus(@NonNull final Integer status,
                                                       @NonNull final String message,
                                                       final List<T> data,
                                                       final long total) {
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setStatus(status);
        responseBodyBean.setMessage(message);
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * <p>Highly customizable response. Status might be any HttpStatus&#39; code value. </p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used in ExceptionControllerAdvice.</p>
     *
     * @param <T>     the response body data type
     * @param status  status code
     * @param message message to be responded
     * @param data    data to be responded
     * @param total   the total
     * @return response body
     * @throws BaseException the base exception
     */
    public static <T> PageResponseBodyBean<T> setResponse(@NonNull final Integer status,
                                                          @NonNull final String message,
                                                          final List<T> data,
                                                          final long total)
            throws BaseException {
        if (!HttpStatus.valueOf(status).is2xxSuccessful()) {
            throw new BaseException(status, message, data);
        }
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setStatus(status);
        responseBodyBean.setMessage(message);
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * Respond null data, and status is OK.
     *
     * @param <T> the response body data type
     * @return response body
     */
    public static <T> PageResponseBodyBean<T> ofSuccess() {
        return new PageResponseBodyBean<>();
    }

    /**
     * Respond data and status is OK.
     *
     * @param <T>   the response body data type
     * @param data  data to be responded to client.
     * @param total the total
     * @return response body
     */
    public static <T> PageResponseBodyBean<T> ofSuccess(final List<T> data,
                                                        final long total) {
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is OK.
     *
     * @param <T>     the response body data type
     * @param message message to be responded
     * @return response body
     */
    public static <T> PageResponseBodyBean<T> ofSuccess(@NonNull final String message) {
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        return responseBodyBean;
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
    public static <T> PageResponseBodyBean<T> ofSuccess(final List<T> data,
                                                        @NonNull final String message,
                                                        final long total) {
        PageResponseBodyBean<T> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <T>     the response body data type
     * @param message message to be responded.
     * @return response body
     * @throws InternalServerException the business exception
     */
    public static <T> PageResponseBodyBean<T> ofFailure(@NonNull final String message) throws InternalServerException {
        throw new InternalServerException(message);
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <T>  the response body data type
     * @param data data to be responded
     * @return response body
     * @throws InternalServerException the business exception
     */
    public static <T> PageResponseBodyBean<T> ofFailure(final List<T> data) throws InternalServerException {
        throw new InternalServerException(data);
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
    public static <T> PageResponseBodyBean<T> ofFailure(final List<T> data,
                                                        @NonNull final String message) throws InternalServerException {
        throw new InternalServerException(data, message);
    }

    /**
     * Respond an ERROR(500).
     *
     * @param <T> the response body data type
     * @return response body
     * @throws BaseException the base exception
     */
    public static <T> PageResponseBodyBean<T> ofError() throws BaseException {
        return setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                           null, 0);
    }

    /**
     * Respond a custom error.
     *
     * @param <T>    the response body data type
     * @param status Error status, not OK(200)
     * @return response body
     * @throws BaseException the base exception
     */
    public static <T> PageResponseBodyBean<T> ofError(@NonNull final HttpStatus status)
            throws BaseException {
        return setResponse(status.value(), status.getReasonPhrase(), null, 0);
    }

    /**
     * Response an exception.
     *
     * @param <T>       the response body data type
     * @param <B>       Subclass of {@link BaseException}
     * @param throwable exception
     * @return the response body bean
     * @throws BaseException the base exception
     */
    public static <T, B extends BaseException> PageResponseBodyBean<T> ofException(@NonNull final B throwable)
            throws BaseException {
        throw throwable;
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
    public static <T> JSON of(@NonNull String message,
                              @Nullable List<T> data,
                              long total, @NonNull Integer status) {
        val responseBodyBean = PageResponseBodyBean.ofStatus(status, message, data, total);
        val config = new JSONConfig();
        config.setIgnoreNullValue(false).setDateFormat("yyyy-MM-dd HH:mm:ss");
        return JSONUtil.parse(responseBodyBean, config);
    }
}
