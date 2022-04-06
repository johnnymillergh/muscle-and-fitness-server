package com.jmsoftware.maf.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * <h1>SecurityException</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date 3/12/20 3:15 PM
 **/
public class SecurityException extends BaseException {
    @Serial
    private static final long serialVersionUID = -767157443094687237L;

    public SecurityException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public SecurityException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public SecurityException(HttpStatus httpStatus, String message, Object data) {
        super(httpStatus.value(), message, data);
    }
}
