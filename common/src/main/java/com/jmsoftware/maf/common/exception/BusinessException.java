package com.jmsoftware.maf.common.exception;

import com.jmsoftware.maf.common.constant.HttpStatus;
import com.jmsoftware.maf.common.constant.IUniversalStatus;

/**
 * <h1>BusinessException</h1>
 * <p>
 * Business Exception
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
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

    public BusinessException(IUniversalStatus status) {
        super(status);
    }

    public BusinessException(IUniversalStatus status, Object data) {
        super(status, data);
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
