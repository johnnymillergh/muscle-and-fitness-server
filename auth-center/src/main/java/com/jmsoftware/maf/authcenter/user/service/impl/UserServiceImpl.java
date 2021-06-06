package com.jmsoftware.maf.authcenter.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.authcenter.user.entity.GetUserStatusPayload;
import com.jmsoftware.maf.authcenter.user.entity.UserPersistence;
import com.jmsoftware.maf.authcenter.user.mapper.UserMapper;
import com.jmsoftware.maf.authcenter.user.service.UserService;
import com.jmsoftware.maf.common.domain.authcenter.user.*;
import com.jmsoftware.maf.common.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <h1>UserServiceImpl</h1>
 * <p>
 * Service implementation of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊)
 * @date 2020-05-10 12:08:28
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user-service-cache")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPersistence> implements UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    @Override
    public GetUserByLoginTokenResponse getUserByLoginToken(@NotBlank String loginToken) {
        LambdaQueryWrapper<UserPersistence> wrapper = Wrappers.lambdaQuery();
        wrapper.and(queryWrapper -> queryWrapper.eq(UserPersistence::getUsername, loginToken)
                .or()
                .eq(UserPersistence::getEmail, loginToken)
                .or()
                .eq(UserPersistence::getCellphone, loginToken));
        val userPersistence = this.getBaseMapper().selectOne(wrapper);
        if (ObjectUtil.isNull(userPersistence)) {
            return null;
        }
        val response = new GetUserByLoginTokenResponse();
        BeanUtil.copyProperties(userPersistence, response);
        return response;
    }

    @Override
    public SignupResponse saveUserForSignup(@Valid SignupPayload payload) {
        val userPersistence = new UserPersistence();
        userPersistence.setUsername(payload.getUsername());
        userPersistence.setEmail(payload.getEmail());
        userPersistence.setPassword(bCryptPasswordEncoder.encode(payload.getPassword()));
        userPersistence.setStatus(UserStatus.ENABLED.getValue());
        this.save(userPersistence);
        log.warn("Saved user for signup. {}", userPersistence);
        val response = new SignupResponse();
        response.setUserId(userPersistence.getId());
        return response;
    }

    @Override
    public LoginResponse login(@Valid LoginPayload payload) throws SecurityException {
        val user = this.getUserByLoginToken(payload.getLoginToken());
        if (ObjectUtil.isNull(user)) {
            throw new SecurityException(HttpStatus.UNAUTHORIZED);
        }
        log.info("User: {}", user);
        boolean matched = bCryptPasswordEncoder.matches(payload.getPassword(), user.getPassword());
        if (matched) {
            String jwt = jwtService.createJwt(payload.getRememberMe(), user.getId(), user.getUsername(), null, null);
            val response = new LoginResponse();
            response.setGreeting(messageSource.getMessage(("greeting"), null, LocaleContextHolder.getLocale()));
            response.setJwt(jwt);
            return response;
        }
        throw new SecurityException(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public boolean logout(HttpServletRequest request) throws SecurityException {
        jwtService.invalidateJwt(request);
        return true;
    }

    @Override
    public String getUserStatus(@Valid @NotNull GetUserStatusPayload payload) {
        return UserStatus.ofValue(payload.getStatus()).getDescription();
    }
}
