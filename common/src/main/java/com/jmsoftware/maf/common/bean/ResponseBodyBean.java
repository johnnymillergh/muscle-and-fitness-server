package com.jmsoftware.maf.common.bean;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jmsoftware.maf.common.exception.BaseException;
import com.jmsoftware.maf.common.exception.BusinessException;
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
 * @param <ResponseBodyDataType> the response body data type
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 2/27/20 9:24 AM
 */
@Data
@SuppressWarnings("unused")
public class ResponseBodyBean<ResponseBodyDataType> implements Serializable {
    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 4645469240048361965L;

    /**
     * The Timestamp.
     */
    @Setter(AccessLevel.NONE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
    /**
     * Default status is 200 OK.
     */
    Integer status = HttpStatus.OK.value();
    /**
     * The Message. Default: 200 OK.
     */
    String message = HttpStatus.OK.getReasonPhrase();
    /**
     * The Data.
     */
    ResponseBodyDataType data;

    /**
     * <p>Respond to client with IUniversalStatus (status may be OK or other).</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param <ResponseBodyDataType> the type parameter
     * @param status                 IUniversalStatus
     * @return response body for ExceptionControllerAdvice javax.servlet.http.HttpServletResponse, Exception)
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final HttpStatus status) {
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setStatus(status.value());
        responseBodyBean.setMessage(status.getReasonPhrase());
        return responseBodyBean;
    }

    /**
     * Of status response body bean.
     *
     * @param <ResponseBodyDataType> the type parameter
     * @param status                 the status
     * @param message                the message
     * @return the response body bean
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final HttpStatus status,
                                                                                         @NonNull final String message) {
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
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
     * @param <ResponseBodyDataType> the response body data type
     * @param status                 IUniversalStatus
     * @param data                   data to be responded to client
     * @return response body for ExceptionControllerAdvice
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final HttpStatus status,
                                                                                         final ResponseBodyDataType data) {
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
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
     * @param <ResponseBodyDataType> the response body data type
     * @param status                 status code
     * @param message                message to be responded
     * @param data                   data to be responded
     * @return response body for ExceptionControllerAdvice
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final Integer status,
                                                                                         @NonNull final String message,
                                                                                         final ResponseBodyDataType data) {
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
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
     * @param <ResponseBodyDataType> the response body data type
     * @param status                 status code
     * @param message                message to be responded
     * @param data                   data to be responded
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> setResponse(@NonNull final Integer status,
                                                                                            @NonNull final String message,
                                                                                            final ResponseBodyDataType data)
            throws BaseException {
        if (!HttpStatus.valueOf(status).is2xxSuccessful()) {
            throw new BaseException(status, message, data);
        }
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setStatus(status);
        responseBodyBean.setMessage(message);
        responseBodyBean.setData(data);
        return responseBodyBean;
    }

    /**
     * Respond null data, and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofSuccess() {
        return new ResponseBodyBean<>();
    }

    /**
     * Respond data and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded to client.
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofSuccess(final ResponseBodyDataType data) {
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setData(data);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param message                message to be responded
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofSuccess(@NonNull final String message) {
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        return responseBodyBean;
    }

    /**
     * Respond data, message and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded
     * @param message                message to be responded
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofSuccess(final ResponseBodyDataType data,
                                                                                          @NonNull final String message) {
        ResponseBodyBean<ResponseBodyDataType> responseBodyBean = new ResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        responseBodyBean.setData(data);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param message                message to be responded.
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofFailure(@NonNull final String message) throws BusinessException {
        throw new BusinessException(message);
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofFailure(final ResponseBodyDataType data) throws BusinessException {
        throw new BusinessException(data);
    }

    /**
     * Respond data and message, and status if FAILURE(464).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded
     * @param message                message to be responded
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofFailure(final ResponseBodyDataType data,
                                                                                          @NonNull final String message) throws BusinessException {
        throw new BusinessException(data, message);
    }

    /**
     * Respond an ERROR(500).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofError() throws BaseException {
        return setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                           null);
    }

    /**
     * Respond a custom error.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param status                 Error status, not OK(200)
     * @return response body
     */
    public static <ResponseBodyDataType> ResponseBodyBean<ResponseBodyDataType> ofError(@NonNull final HttpStatus status)
            throws BaseException {
        return setResponse(status.value(), status.getReasonPhrase(), null);
    }

    /**
     * Response an exception.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param <BaseThrowable>        Sub class of {@link BaseException}
     * @param throwable              exception
     * @return the response body bean
     */
    public static <ResponseBodyDataType, BaseThrowable extends BaseException> ResponseBodyBean<ResponseBodyDataType> ofException(@NonNull final BaseThrowable throwable)
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
    public static JSON of(@NonNull String message, @Nullable Object data, @NonNull Integer status) {
        val responseBodyBean = ResponseBodyBean.ofStatus(status, message, data);
        val config = new JSONConfig();
        config.setIgnoreNullValue(false).setDateFormat("yyyy-MM-dd HH:mm:ss");
        return JSONUtil.parse(responseBodyBean, config);
    }
}
