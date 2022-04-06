package com.jmsoftware.maf.common.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * <h1>ResourceNotFoundException</h1>
 * <p>
 * Resource not found exception
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 2/19/2022 5:05 PM
 **/
public class ResourceNotFoundException extends BaseException {
    @Serial
    private static final long serialVersionUID = -1706570460684455863L;

    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ResourceNotFoundException(Object data) {
        super(data, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(Object data, String message) {
        super(HttpStatus.NOT_FOUND.value(), message, data);
    }
}
