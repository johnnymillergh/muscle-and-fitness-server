package com.jmsoftware.maf.common.exception;

import com.jmsoftware.maf.common.constant.HttpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>SecurityException</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/12/20 3:15 PM
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityException extends BaseException {
    private static final long serialVersionUID = -767157443094687237L;

    public SecurityException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public SecurityException(HttpStatus httpStatus, String message, Object data) {
        super(httpStatus.getCode(), message, data);
    }
}
