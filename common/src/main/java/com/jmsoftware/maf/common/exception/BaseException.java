package com.jmsoftware.maf.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * <h1>BaseException</h1>
 * <p>
 * Base exception.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2019-03-23 16:23
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends Exception {
    @Serial
    private static final long serialVersionUID = 5049763892480652887L;

    /**
     * Code is REQUIRED. Default code is 500 (Internal Server Error).
     */
    private Integer code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    /**
     * Message is REQUIRED. Default message is: Error. A generic status for an error in the server itself.
     */
    private String message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    private Object data;

    public BaseException(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }

    public BaseException(Object data, HttpStatus httpStatus) {
        this(httpStatus);
        this.data = data;
    }

    public BaseException(HttpStatus status, Object data) {
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
