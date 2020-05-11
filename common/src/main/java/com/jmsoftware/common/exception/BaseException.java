package com.jmsoftware.common.exception;

import com.jmsoftware.common.constant.HttpStatus;
import com.jmsoftware.common.constant.IUniversalStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>BaseException</h1>
 * <p>
 * Base exception.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 16:23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends Exception {
    private static final long serialVersionUID = 5049763892480652887L;

    /**
     * Code is REQUIRED. Default code is 464.
     */
    private Integer code = HttpStatus.FAILURE.getCode();
    /**
     * Message is REQUIRED. Default message is: Error. A generic status for an error in the server itself.
     */
    private String message;
    private Object data;

    public BaseException(HttpStatus httpStatus) {
        this.code = httpStatus.getCode();
        this.message = httpStatus.getMessage();
    }

    public BaseException(Object data, HttpStatus httpStatus) {
        this(httpStatus);
        this.data = data;
    }

    public BaseException(IUniversalStatus status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public BaseException(IUniversalStatus status, Object data) {
        this(status);
        this.data = data;
    }

    public BaseException(HttpStatus httpStatus, String message) {
        this(httpStatus);
        this.message = message;
    }

    public BaseException(String message) {
        this.message = message;
    }

    public BaseException(Integer status, String message, Object data) {
        this.code = status;
        this.message = message;
        this.data = data;
    }

    public BaseException(String message, Throwable throwable) {
        this(message);
        super.setStackTrace(throwable.getStackTrace());
    }

    public BaseException(Object data) {
        this.data = data;
    }

    public BaseException(Throwable throwable) {
        this(throwable.getMessage());
        super.setStackTrace(throwable.getStackTrace());
    }

    public BaseException(Object data, String message) {
        this.data = data;
        this.message = message;
    }
}
