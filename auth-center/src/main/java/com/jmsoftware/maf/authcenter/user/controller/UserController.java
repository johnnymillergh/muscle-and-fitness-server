package com.jmsoftware.maf.authcenter.user.controller;

import com.jmsoftware.maf.authcenter.user.service.UserService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.user.LoginPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.LoginResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.SignupPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.SignupResponse;
import com.jmsoftware.maf.common.exception.SecurityException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"User API"})
public class UserController {
    private final UserService userService;

    @PostMapping("/users/login")
    @ApiOperation(value = "Login", notes = "Login")
    public ResponseBodyBean<LoginResponse> login(@Valid @RequestBody LoginPayload payload) throws SecurityException {
        return ResponseBodyBean.ofSuccess(userService.login(payload));
    }

    @PostMapping("/users/signup")
    @ApiOperation(value = "Save user for signup", notes = "Save user for signup")
    public ResponseBodyBean<SignupResponse> signup(@Valid @RequestBody SignupPayload payload) {
        return ResponseBodyBean.ofSuccess(userService.saveUserForSignup(payload));
    }
}
