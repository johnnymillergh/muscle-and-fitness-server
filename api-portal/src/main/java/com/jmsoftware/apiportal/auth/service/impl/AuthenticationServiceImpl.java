package com.jmsoftware.apiportal.auth.service.impl;

import com.jmsoftware.apiportal.auth.entity.LoginPayload;
import com.jmsoftware.apiportal.auth.entity.LoginResponse;
import com.jmsoftware.apiportal.auth.entity.RegisterPayload;
import com.jmsoftware.apiportal.auth.service.AuthenticationService;
import com.jmsoftware.apiportal.universal.domain.UserPO;
import com.jmsoftware.apiportal.universal.service.JwtService;
import com.jmsoftware.apiportal.universal.service.UserService;
import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public void register(RegisterPayload payload) {
        val userPo = new UserPO();
        userPo.setUsername(payload.getUsername());
        userPo.setEmail(payload.getEmail());
        userPo.setPassword(encoder.encode(payload.getPassword()));
        // TODO: user service should be in the `auth-center`
        userService.saveUser(userPo);
    }

    @Override
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
