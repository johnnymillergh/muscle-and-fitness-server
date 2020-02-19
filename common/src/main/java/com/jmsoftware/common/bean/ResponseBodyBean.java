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
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:52
 **/
@Getter
@Builder
@ToString
@SuppressWarnings("unused")
public class ResponseBodyBean<T> implements Serializable {
    private static final long serialVersionUID = 4645469240048361965L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;
    /**
     * Default status is OK[200]
     */
    private Integer status;
    private String message;
    private T data;

    /**
     * <p>Respond to client with IUniversalStatus (status may be OK or other).</p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used by controller or service or other class, only provided for Exception controller
     * .</p>
     *
     * @param status IUniversalStatus
     * @return response body for ExceptionControllerAdvice
     * javax.servlet.http.HttpServletResponse, Exception)
     */
    public static <T> ResponseBodyBean<T> ofStatus(IUniversalStatus status) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
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
     * @param status IUniversalStatus
     * @param data   data to be responded to client
     * @return response body for ExceptionControllerAdvice
     */
    public static <T> ResponseBodyBean<T> ofStatus(IUniversalStatus status, T data) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
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
     * @param status  status code
     * @param message message to be responded
     * @param data    data to be responded
     * @return response body for ExceptionControllerAdvice
     */
    public static <T> ResponseBodyBean<T> ofStatus(Integer status, String message, T data) {
        return ResponseBodyBean.<T>builder().timestamp(new Date()).status(status).message(message).data(data).build();
    }

    /**
     * <p>Highly customizable response. Status might be any HttpStatus&#39; code value. </p>
     * <p><strong>ATTENTION:</strong></p>
     * <p>This method CANNOT be used in ExceptionControllerAdvice.</p>
     *
     * @param status  status code
     * @param message message to be responded
     * @param data    data to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> setResponse(Integer status, String message, T data) {
        if (!HttpStatus.OK.getCode().equals(status)) {
            throw new BaseException(status, message, data);
        }
        return ResponseBodyBean.<T>builder().timestamp(new Date()).status(status).message(message).data(data).build();
    }

    /**
     * Respond data and status is OK.
     *
     * @param data data to be responded to client.
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofData(T data) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .build();
    }

    /**
     * Respond a message and status id OK.
     *
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofMessage(String message) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .message(message)
                .build();
    }

    /**
     * Respond data, message and status is OK.
     *
     * @param data    data to be responded
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofDataAndMessage(T data, String message) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .message(message)
                .build();
    }

    /**
     * Respond data, and status is OK.
     *
     * @param data data to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofSuccess(T data) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .build();
    }

    /**
     * Respond a message and status is OK.
     *
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofSuccess(String message) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .message(message)
                .build();
    }

    /**
     * Respond data, message and status is OK.
     *
     * @param data    data to be responded
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofSuccess(T data, String message) {
        return ResponseBodyBean.<T>builder().timestamp(new Date())
                .status(HttpStatus.OK.getCode())
                .data(data)
                .message(message)
                .build();
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param message message to be responded.
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofFailure(String message) {
        throw new BusinessException(message);
    }

    /**
     * Respond a message and status is FAILURE(464).
     *
     * @param data data to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofFailure(Object data) {
        throw new BusinessException(data);
    }

    /**
     * Respond data and message, and status if FAILURE(464).
     *
     * @param data    data to be responded
     * @param message message to be responded
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofFailure(T data, String message) {
        throw new BusinessException(data, message);
    }

    /**
     * Respond an ERROR(500).
     *
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofError() {
        return setResponse(HttpStatus.ERROR.getCode(), HttpStatus.ERROR.getMessage(), null);
    }

    /**
     * Respond a custom error.
     *
     * @param status Error status, not OK(200)
     * @return response body
     */
    public static <T> ResponseBodyBean<T> ofError(IUniversalStatus status) {
        return setResponse(status.getCode(), status.getMessage(), null);
    }

    /**
     * Response an exception.
     *
     * @param t   exception
     * @param <T> Sub class of {@link BaseException}
     * @return Exception information
     */
    public static <T extends BaseException> ResponseBodyBean<Object> ofException(T t) {
        throw new BaseException(t.getCode(), t.getMessage(), t.getData());
    }
}
