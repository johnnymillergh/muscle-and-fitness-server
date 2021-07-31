package com.jmsoftware.maf.authcenter.user.controller;

import com.jmsoftware.maf.authcenter.user.entity.GetUserPageListPayload;
import com.jmsoftware.maf.authcenter.user.entity.GetUserStatusPayload;
import com.jmsoftware.maf.authcenter.user.entity.persistence.User;
import com.jmsoftware.maf.authcenter.user.service.UserService;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>UserRemoteApiController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/10/20 12:36 PM
 **/
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-remote-api")
@Api(tags = {"User Remote API"})
public class UserRemoteApiController {
    private final UserService userService;

    @GetMapping("/users/{loginToken}")
    @ApiOperation(value = "Get user by login token", notes = "Get user by login token (Remote)")
    public ResponseBodyBean<GetUserByLoginTokenResponse> getUserByLoginToken(@PathVariable String loginToken) {
        return ResponseBodyBean.ofSuccess(this.userService.getUserByLoginToken(loginToken));
    }

    @GetMapping("/users")
    @ApiOperation(value = "Retrieve user page list", notes = "Retrieve user page list")
    public PageResponseBodyBean<User> getUserPageList(@Valid GetUserPageListPayload payload) {
        return this.userService.getUserPageList(payload);
    }

    @GetMapping("/users/status")
    @ApiOperation(value = "Retrieve user page list", notes = "Retrieve user page list")
    public ResponseBodyBean<String> getUserStatus(@Valid GetUserStatusPayload payload) {
        return ResponseBodyBean.ofSuccess(this.userService.getUserStatus(payload), "Correct enum value");
    }
}
