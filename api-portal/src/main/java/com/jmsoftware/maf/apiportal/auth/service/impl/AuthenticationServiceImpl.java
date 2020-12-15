package com.jmsoftware.maf.apiportal.auth.service.impl;

import com.jmsoftware.maf.apiportal.auth.entity.LoginPayload;
import com.jmsoftware.maf.apiportal.auth.entity.LoginResponse;
import com.jmsoftware.maf.apiportal.auth.entity.RegisterPayload;
import com.jmsoftware.maf.apiportal.auth.service.AuthenticationService;
import com.jmsoftware.maf.apiportal.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.apiportal.universal.service.JwtService;
import com.jmsoftware.maf.common.domain.authcenter.user.SaveUserForRegisteringPayload;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <h1>AuthenticationServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 5/8/20 2:04 PM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthCenterRemoteApi authCenterRemoteApi;

    @Override
    public Long register(RegisterPayload payload) {
        val saveUserForRegisteringPayload = new SaveUserForRegisteringPayload();
        saveUserForRegisteringPayload.setUsername(payload.getUsername());
        saveUserForRegisteringPayload.setEmail(payload.getEmail());
        saveUserForRegisteringPayload.setEncodedPassword(encoder.encode(payload.getPassword()));
        val response = authCenterRemoteApi.saveUserForRegister(saveUserForRegisteringPayload);
        log.info("Registered an new user account. User ID: {}", response.getData().getUserId());
        return response.getData().getUserId();
    }

    @Override
    @SneakyThrows
    public LoginResponse login(LoginPayload payload) {
        val authenticationToken = new UsernamePasswordAuthenticationToken(payload.getLoginToken(),
                                                                          payload.getPassword());
        val authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        val jwt = jwtService.createJwt(authentication, payload.getRememberMe());
        val loginResponse = new LoginResponse();
        loginResponse.setJwt(jwt);
        return loginResponse;
    }
}
