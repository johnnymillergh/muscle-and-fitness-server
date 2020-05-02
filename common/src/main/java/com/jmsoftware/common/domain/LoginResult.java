package com.jmsoftware.common.domain;

import lombok.Data;

/**
 * <h1>LoginResult</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/14/20 9:05 AM
 **/
@Data
public class LoginResult {
    private String uid;
    private String token;
}
