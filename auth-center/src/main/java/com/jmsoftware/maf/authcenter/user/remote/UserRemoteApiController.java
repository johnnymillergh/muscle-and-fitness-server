package com.jmsoftware.maf.authcenter.user.remote;

import com.jmsoftware.maf.authcenter.user.service.UserService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.SaveUserForRegisteringPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.SaveUserForRegisteringResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>UserRemoteApiController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/10/20 12:36 PM
 **/
@RestController
@RequestMapping("/user-remote-api")
@RequiredArgsConstructor
@Api(tags = {"User Remote API Controller"})
public class UserRemoteApiController {
    private final UserService userService;

    @PostMapping("/get-user-by-login-token")
    @ApiOperation(value = "/get-user-by-login-token", notes = "Get user by login token")
    public ResponseBodyBean<GetUserByLoginTokenResponse> getUserByLoginToken(@Valid @RequestBody GetUserByLoginTokenPayload payload) {
        return ResponseBodyBean.ofSuccess(userService.getUserByLoginToken(payload.getLoginToken()));
    }

    @PostMapping("/save-user-for-registering")
    public ResponseBodyBean<SaveUserForRegisteringResponse> saveUserForRegistering(@Valid @RequestBody SaveUserForRegisteringPayload payload) {
        return ResponseBodyBean.ofSuccess(userService.saveUserForRegistering(payload));
    }
}
