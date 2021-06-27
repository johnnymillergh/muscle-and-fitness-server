package com.jmsoftware.maf.common.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.jmsoftware.maf.common.exception.BaseException;
import com.jmsoftware.maf.common.exception.BusinessException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * <h1>PageResponseBodyBean</h1>
 * <p>
 * Page Response body bean.
 *
 * @param <ResponseBodyDataType> the response body data type
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 4:24 PM
 */
@Data
@SuppressWarnings("unused")
public class PageResponseBodyBean<ResponseBodyDataType> implements Serializable {
    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 4645461634548783641L;

    /**
     * The Timestamp.
     */
    @Setter(AccessLevel.NONE)
    private final LocalDateTime timestamp = LocalDateTime.now();
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
    private Collection<ResponseBodyDataType> list =  Collections.emptyList();
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
     * @param <ResponseBodyDataType> the type parameter
     * @param status                 IUniversalStatus
     * @return response body for ExceptionControllerAdvice javax.servlet.http.HttpServletResponse, Exception)
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final HttpStatus status) {
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
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
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final HttpStatus status,
                                                                                             @NonNull final String message) {
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
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
     * @param total                  the total
     * @return response body for ExceptionControllerAdvice
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final HttpStatus status,
                                                                                             final Collection<ResponseBodyDataType> data,
                                                                                             final long total) {
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
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
     * @param <ResponseBodyDataType> the response body data type
     * @param status                 status code
     * @param message                message to be responded
     * @param data                   data to be responded
     * @param total                  the total
     * @return response body for ExceptionControllerAdvice
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofStatus(@NonNull final Integer status,
                                                                                             @NonNull final String message,
                                                                                             final Collection<ResponseBodyDataType> data,
                                                                                             final long total) {
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
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
     * @param <ResponseBodyDataType> the response body data type
     * @param status                 status code
     * @param message                message to be responded
     * @param data                   data to be responded
     * @param total                  the total
     * @return response body
     * @throws BaseException the base exception
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> setResponse(@NonNull final Integer status,
                                                                                                @NonNull final String message,
                                                                                                final Collection<ResponseBodyDataType> data,
                                                                                                final long total)
            throws BaseException {
        if (!HttpStatus.valueOf(status).is2xxSuccessful()) {
            throw new BaseException(status, message, data);
        }
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setStatus(status);
        responseBodyBean.setMessage(message);
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * Respond null data, and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @return response body
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofSuccess() {
        return new PageResponseBodyBean<>();
    }

    /**
     * Respond data and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded to client.
     * @param total                  the total
     * @return response body
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofSuccess(final Collection<ResponseBodyDataType> data,
                                                                                              final long total) {
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param message                message to be responded
     * @return response body
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofSuccess(@NonNull final String message) {
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        return responseBodyBean;
    }

    /**
     * Respond data, message and status is OK.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded
     * @param message                message to be responded
     * @param total                  the total
     * @return response body
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofSuccess(final Collection<ResponseBodyDataType> data,
                                                                                              @NonNull final String message,
                                                                                              final long total) {
        PageResponseBodyBean<ResponseBodyDataType> responseBodyBean = new PageResponseBodyBean<>();
        responseBodyBean.setMessage(message);
        responseBodyBean.setList(data);
        responseBodyBean.setTotal(total);
        return responseBodyBean;
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param message                message to be responded.
     * @return response body
     * @throws BusinessException the business exception
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofFailure(@NonNull final String message) throws BusinessException {
        throw new BusinessException(message);
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded
     * @return response body
     * @throws BusinessException the business exception
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofFailure(final Collection<ResponseBodyDataType> data) throws BusinessException {
        throw new BusinessException(data);
    }

    /**
     * Respond data and message, and status if FAILURE(464).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param data                   data to be responded
     * @param message                message to be responded
     * @return response body
     * @throws BusinessException the business exception
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofFailure(final Collection<ResponseBodyDataType> data,
                                                                                              @NonNull final String message) throws BusinessException {
        throw new BusinessException(data, message);
    }

    /**
     * Respond an ERROR(500).
     *
     * @param <ResponseBodyDataType> the response body data type
     * @return response body
     * @throws BaseException the base exception
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofError() throws BaseException {
        return setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                           null, 0);
    }

    /**
     * Respond a custom error.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param status                 Error status, not OK(200)
     * @return response body
     * @throws BaseException the base exception
     */
    public static <ResponseBodyDataType> PageResponseBodyBean<ResponseBodyDataType> ofError(@NonNull final HttpStatus status)
            throws BaseException {
        return setResponse(status.value(), status.getReasonPhrase(), null, 0);
    }

    /**
     * Response an exception.
     *
     * @param <ResponseBodyDataType> the response body data type
     * @param <BaseThrowable>        Sub class of {@link BaseException}
     * @param throwable              exception
     * @return the response body bean
     * @throws BaseException the base exception
     */
    public static <ResponseBodyDataType, BaseThrowable extends BaseException> PageResponseBodyBean<ResponseBodyDataType> ofException(@NonNull final BaseThrowable throwable)
            throws BaseException {
        throw throwable;
    }

    /**
     * Of json.
     *
     * @param <ResponseBodyDataType> the type parameter
     * @param message                the message
     * @param data                   the data
     * @param total                  the total
     * @param status                 the status
     * @return the json
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/22/2020 10:16 AM
     */
    public static <ResponseBodyDataType> JSON of(@NonNull String message,
                                                 @Nullable Collection<ResponseBodyDataType> data,
                                                 long total, @NonNull Integer status) {
        val responseBodyBean = PageResponseBodyBean.ofStatus(status, message, data, total);
        val config = new JSONConfig();
        config.setIgnoreNullValue(false).setDateFormat("yyyy-MM-dd HH:mm:ss");
        return JSONUtil.parse(responseBodyBean, config);
    }
}
