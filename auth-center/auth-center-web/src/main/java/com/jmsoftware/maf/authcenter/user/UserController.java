package com.jmsoftware.maf.authcenter.user;

import com.jmsoftware.maf.authcenter.user.service.UserDomainService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.user.LoginPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.LoginResponse;
import com.jmsoftware.maf.common.domain.authcenter.user.SignupPayload;
import com.jmsoftware.maf.common.domain.authcenter.user.SignupResponse;
import com.jmsoftware.maf.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private final UserDomainService userDomainService;

    @PostMapping("/users/signup")
    public ResponseBodyBean<SignupResponse> signup(@Valid @RequestBody SignupPayload payload) {
        return ResponseBodyBean.ofSuccess(this.userDomainService.saveUserForSignup(payload));
    }

    @PostMapping("/users/login")
    public ResponseBodyBean<LoginResponse> login(@Valid @RequestBody LoginPayload payload) throws SecurityException {
        return ResponseBodyBean.ofSuccess(this.userDomainService.login(payload));
    }

    @PostMapping("/users/logout")
    public ResponseBodyBean<Boolean> logout(HttpServletRequest request) throws SecurityException {
        return ResponseBodyBean.ofSuccess(this.userDomainService.logout(request));
    }
}
