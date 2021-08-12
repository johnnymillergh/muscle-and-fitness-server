package com.jmsoftware.maf.common.exception;

import org.springframework.http.HttpStatus;

/**
 * <h1>BizException</h1>
 * <p>
 * Business Exception
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-02 17:15
 **/
public class BizException extends BaseException {
    private static final long serialVersionUID = 6403325238832002908L;

    public BizException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public BizException(HttpStatus httpStatus, Object data) {
        super(httpStatus, data);
    }

    public BizException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Throwable throwable) {
        super(throwable);
    }

    public BizException(Integer status, String message, Object data) {
        super(status, message, data);
    }

    public BizException(Object data) {
        super(data);
    }

    public BizException(Object data, String message) {
        super(data, message);
    }
}
