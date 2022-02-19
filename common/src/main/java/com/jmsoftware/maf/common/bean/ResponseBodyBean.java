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

/**
 * <h1>ResponseBodyBean</h1>
 * <p>
 * Response body bean.
 *
 * @param <T> the response body data type
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/27/20 9:24 AM
 */
@Data
@SuppressWarnings("unused")
public class ResponseBodyBean<T> implements Serializable {
    private static final long serialVersionUID = 4645469240048361965L;

    /**
     * The Timestamp. Must be annotated by '@JsonFormat', otherwise will cause following error, cuz api-gateway does not
     * know how to convert LocalDateTime.
     * <p>
     * Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2021-06-27
     * 23:08:46'
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern = UniversalDateTime.DATE_TIME_FORMAT)
    private final LocalDateTime timestamp = LocalDateTime.now();
    /**
     * Default status is 200 OK.
     */
    private int status = HttpStatus.OK.value();
    /**
     * The Message. Default: 200 OK.
     */
    private String message = HttpStatus.OK.getReasonPhrase();
    /**
     * The Data.
     */
    private T data;

    private ResponseBodyBean() {
    }

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
    public static <T> ResponseBodyBean<T> ofStatus(@NonNull final HttpStatus status) {
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
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
    public static <T> ResponseBodyBean<T> ofStatus(@NonNull final HttpStatus status, @NonNull final String message) {
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
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
     * @return response body for ExceptionControllerAdvice
     */
    public static <T> ResponseBodyBean<T> ofStatus(@NonNull final HttpStatus status, @Nullable final T data) {
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setStatus(status.value());
        responseBodyBean.setMessage(status.getReasonPhrase());
        responseBodyBean.setData(data);
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
     * @return response body for ExceptionControllerAdvice
     */
    public static <T> ResponseBodyBean<T> ofStatus(final int status, @NonNull final String message,
                                                   @Nullable final T data) {
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setStatus(status);
        responseBodyBean.setMessage(message);
        responseBodyBean.setData(data);
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
     * @return response body
     */
    public static <T> ResponseBodyBean<T> setResponse(final int status, @NonNull final String message,
                                                      @Nullable final T data)
            throws BaseException {
        if (!HttpStatus.valueOf(status).is2xxSuccessful()) {
            throw new BaseException(status, message, data);
        }
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setStatus(status);
        responseBodyBean.setMessage(message);
        responseBodyBean.setData(data);
        return responseBodyBean;
    }

    /**
     * Respond null data, and status is OK.
     *
     * @param <T> the response body data type
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofSuccess() {
        return new ResponseBodyBean<>();
    }

    /**
     * Respond data and status is OK.
     *
     * @param <T>  the response body data type
     * @param data data to be responded to client.
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofSuccess(@Nullable final T data) {
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setData(data);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is OK.
     *
     * @param <T>     the response body data type
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofSuccessMessage(@NonNull final String message) {
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        return responseBodyBean;
    }

    /**
     * Respond data, message and status is OK.
     *
     * @param <T>     the response body data type
     * @param data    data to be responded
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofSuccess(@Nullable final T data,
                                                    @NonNull final String message) {
        ResponseBodyBean<T> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        responseBodyBean.setData(data);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <T>     the response body data type
     * @param message message to be responded.
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofFailureMessage(@NonNull final String message) throws InternalServerException {
        throw new InternalServerException(message);
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <T>  the response body data type
     * @param data data to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofFailure(final T data) throws InternalServerException {
        throw new InternalServerException(data);
    }

    /**
     * Respond data and message, and status if FAILURE(464).
     *
     * @param <T>     the response body data type
     * @param data    data to be responded
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofFailure(final T data, @NonNull final String message) throws InternalServerException {
        throw new InternalServerException(data, message);
    }

    /**
     * Respond an ERROR(500).
     *
     * @param <T> the response body data type
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofError() throws BaseException {
        return setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                           null);
    }

    /**
     * Respond a custom error.
     *
     * @param <T>    the response body data type
     * @param status Error status, not OK(200)
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofError(@NonNull final HttpStatus status)
            throws BaseException {
        return setResponse(status.value(), status.getReasonPhrase(), null);
    }

    /**
     * Response an exception.
     *
     * @param <T>       the response body data type
     * @param <B>       Sub-class of {@link BaseException}
     * @param throwable exception
     * @return the response body bean
     */
    public static <T, B extends BaseException> ResponseBodyBean<T> ofException(@NonNull final B throwable)
            throws BaseException {
        throw throwable;
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
    public static JSON of(@NonNull String message, @Nullable Object data, int status) {
        val responseBodyBean = ResponseBodyBean.ofStatus(status, message, data);
        val config = new JSONConfig();
        config.setIgnoreNullValue(false).setDateFormat(UniversalDateTime.DATE_TIME_FORMAT);
        return JSONUtil.parse(responseBodyBean, config);
    }
}
