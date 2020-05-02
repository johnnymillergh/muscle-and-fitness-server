package com.jmsoftware.common.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <h1>LoginPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/14/20 9:03 AM
 **/
@Data
public class LoginPayload {
    @NotEmpty
    private String uid;
    @NotEmpty
    private String password;
}
