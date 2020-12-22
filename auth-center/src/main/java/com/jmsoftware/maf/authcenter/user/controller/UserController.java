package com.jmsoftware.maf.authcenter.user.controller;

import com.jmsoftware.maf.authcenter.user.service.UserService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.user.LoginPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>UserController</h1>
 * <p>
 * Controller implementation of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊)
 * @date 2020-05-10 12:08:28
 */
@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users/login")
    public ResponseBodyBean<LoginResponse> login(@Valid @RequestBody LoginPayload payload) {
        return ResponseBodyBean.ofSuccess(userService.login(payload));
    }
}
