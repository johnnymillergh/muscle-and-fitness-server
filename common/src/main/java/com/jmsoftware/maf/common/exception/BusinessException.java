package com.jmsoftware.maf.common.exception;

import org.springframework.http.HttpStatus;

/**
 * <h1>BusinessException</h1>
 * <p>
 * Business Exception
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:15
 **/
public class BusinessException extends BaseException {
    private static final long serialVersionUID = 6403325238832002908L;

    public BusinessException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public BusinessException(HttpStatus httpStatus, Object data) {
        super(httpStatus, data);
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
    }

    public BusinessException(Integer status, String message, Object data) {
        super(status, message, data);
    }

    public BusinessException(Object data) {
        super(data);
    }

    public BusinessException(Object data, String message) {
        super(data, message);
    }
}
