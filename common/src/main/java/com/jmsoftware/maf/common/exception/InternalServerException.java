package com.jmsoftware.maf.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * <h1>InternalServerException</h1>
 * <p>
 * Business Exception
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2019-03-02 17:15
 **/
public class InternalServerException extends BaseException {
    @Serial
    private static final long serialVersionUID = 6403325238832002908L;

    public InternalServerException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public InternalServerException(HttpStatus httpStatus, Object data) {
        super(httpStatus, data);
    }

    public InternalServerException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(Throwable throwable) {
        super(throwable);
    }

    public InternalServerException(Integer status, String message, Object data) {
        super(status, message, data);
    }

    public InternalServerException(Object data) {
        super(data);
    }

    public InternalServerException(Object data, String message) {
        super(data, message);
    }
}
