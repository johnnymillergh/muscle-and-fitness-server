package com.jmsoftware.maf.apiportal.auth.controller;

import com.jmsoftware.maf.apiportal.auth.entity.LoginPayload;
import com.jmsoftware.maf.apiportal.auth.entity.LoginResponse;
import com.jmsoftware.maf.apiportal.auth.entity.RegisterPayload;
import com.jmsoftware.maf.apiportal.auth.service.AuthenticationService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>AuthenticationController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/8/20 11:06 AM
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
@Api(tags = {"Authentication Controller"})
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ApiOperation(value = "/register", notes = "Register (create an account)")
    public ResponseBodyBean<Long> register(@Valid @RequestBody RegisterPayload payload) {
        return ResponseBodyBean.ofSuccess(authenticationService.register(payload));
    }

    @PostMapping("/login")
    @ApiOperation(value = "/login", notes = "Login")
    public ResponseBodyBean<LoginResponse> login(@Valid @RequestBody LoginPayload payload) {
        return ResponseBodyBean.ofSuccess(authenticationService.login(payload));
    }
}
