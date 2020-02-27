package com.jmsoftware.common.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.constant.IUniversalStatus;
import com.jmsoftware.common.exception.BaseException;
import com.jmsoftware.common.exception.BusinessException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <h1>ResponseBodyBean</h1>
 * <p>
 * Response body bean.
 *
 * @param <T> the body type
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 2/27/20 9:24 AM
 */
@Getter
@Builder
@ToString
@SuppressWarnings("unused")
public class ResponseBodyBean<T> implements Serializable {
    /**
     * The constant serialVersionUID.
     */
    private static final long serialVersionUID = 4645469240048361965L;

    /**
     * The Timestamp.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;
    /**
     * Default status is OK[200]
     */
    private Integer status;
    /**
     * The Message.
     */
    private String message;
    /**
     * The Data.
     */
    private T data;

    /**
     * <p>Respond to client with IUniversalStatus (status may be OK or other).</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param <ResponseBodyType> the type parameter
     * @param status             IUniversalStatus
     * @return response body for ExceptionControllerAdvice javax.servlet.http.HttpServletResponse, Exception)
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofStatus(final IUniversalStatus status) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(status.getCode())
                .message(status.getMessage())
                .build();
    }

    /**
     * <p>Respond to client with IUniversalStatus and data.</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param <ResponseBodyType> the type parameter
     * @param status             IUniversalStatus
     * @param data               data to be responded to client
     * @return response body for ExceptionControllerAdvice
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofStatus(final IUniversalStatus status,
                                                                                 final ResponseBodyType data) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(status.getCode())
                .message(status.getMessage())
                .data(data)
                .build();
    }

    /**
     * <p>Highly customizable response. Status might be any HttpStatus&#39; code value.</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param <ResponseBodyType> the type parameter
     * @param status             status code
     * @param message            message to be responded
     * @param data               data to be responded
     * @return response body for ExceptionControllerAdvice
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofStatus(final Integer status,
                                                                                 final String message,
                                                                                 final ResponseBodyType data) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date()).status(status).message(message).data(data).build();
    }

    /**
     * <p>Highly customizable response. Status might be any HttpStatus&#39; code value. </p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used in ExceptionControllerAdvice.</p>
     *
     * @param <ResponseBodyType> the type parameter
     * @param status             status code
     * @param message            message to be responded
     * @param data               data to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> setResponse(final Integer status,
                                                                                    final String message,
                                                                                    final ResponseBodyType data) {
        if (!HttpStatus.OK.getCode().equals(status)) {
            throw new BaseException(status, message, data);
        }
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date()).status(status).message(message).data(data).build();
    }

    /**
     * Respond data and status is OK.
     *
     * @param <ResponseBodyType> the type parameter
     * @param data               data to be responded to client.
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofData(final ResponseBodyType data) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .build();
    }

    /**
     * Respond a message and status id OK.
     *
     * @param <ResponseBodyType> the type parameter
     * @param message            message to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofMessage(final String message) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .message(message)
                .build();
    }

    /**
     * Respond data, message and status is OK.
     *
     * @param <ResponseBodyType> the type parameter
     * @param data               data to be responded
     * @param message            message to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofDataAndMessage(final ResponseBodyType data,
                                                                                         final String message) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .message(message)
                .build();
    }

    /**
     * Respond data, and status is OK.
     *
     * @param <ResponseBodyType> the type parameter
     * @param data               data to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofSuccess(final ResponseBodyType data) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .build();
    }

    /**
     * Respond a message and status is OK.
     *
     * @param <ResponseBodyType> the type parameter
     * @param message            message to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofSuccess(final String message) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .message(message)
                .build();
    }

    /**
     * Respond data, message and status is OK.
     *
     * @param <ResponseBodyType> the type parameter
     * @param data               data to be responded
     * @param message            message to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofSuccess(final ResponseBodyType data,
                                                                                  final String message) {
        return ResponseBodyBean.<ResponseBodyType>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .message(message)
                .build();
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <ResponseBodyType> the type parameter
     * @param message            message to be responded.
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofFailure(final String message) {
        throw new BusinessException(message);
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param <ResponseBodyType> the type parameter
     * @param data               data to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofFailure(final Object data) {
        throw new BusinessException(data);
    }

    /**
     * Respond data and message, and status if FAILURE(464).
     *
     * @param <ResponseBodyType> the type parameter
     * @param data               data to be responded
     * @param message            message to be responded
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofFailure(final ResponseBodyType data,
                                                                                  final String message) {
        throw new BusinessException(data, message);
    }

    /**
     * Respond an ERROR(500).
     *
     * @param <ResponseBodyType> the type parameter
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofError() {
        return setResponse(HttpStatus.ERROR.getCode(), HttpStatus.ERROR.getMessage(), null);
    }

    /**
     * Respond a custom error.
     *
     * @param <ResponseBodyType> the type parameter
     * @param status             Error status, not OK(200)
     * @return response body
     */
    public static <ResponseBodyType> ResponseBodyBean<ResponseBodyType> ofError(final IUniversalStatus status) {
        return setResponse(status.getCode(), status.getMessage(), null);
    }

    /**
     * Response an exception.
     *
     * @param <BaseThrowable> Sub class of {@link BaseException}
     * @param throwable       exception
     * @return Exception information
     */
    public static <BaseThrowable extends BaseException> ResponseBodyBean<Object> ofException(final BaseThrowable throwable) {
        throw new BaseException(throwable.getCode(), throwable.getMessage(), throwable.getData());
    }
}
