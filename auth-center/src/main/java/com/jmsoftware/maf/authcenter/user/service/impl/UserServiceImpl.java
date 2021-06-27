package com.jmsoftware.maf.authcenter.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.authcenter.user.entity.GetUserStatusPayload;
import com.jmsoftware.maf.authcenter.user.entity.persistence.User;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final MessageSource messageSource;

    @Override
    public GetUserByLoginTokenResponse getUserByLoginToken(@NotBlank String loginToken) {
        val wrapper = Wrappers.lambdaQuery(User.class);
        wrapper.and(queryWrapper -> queryWrapper.eq(User::getUsername, loginToken)
                .or()
                .eq(User::getEmail, loginToken)
                .or()
                .eq(User::getCellphone, loginToken));
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
        val user = new User();
        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(payload.getPassword()));
        user.setStatus(UserStatus.ENABLED.getValue());
        this.save(user);
        log.warn("Saved user for signup. {}", user);
        val response = new SignupResponse();
        response.setUserId(user.getId());
        return response;
    }

    @Override
    public LoginResponse login(@Valid LoginPayload payload) throws SecurityException {
        val user = this.getUserByLoginToken(payload.getLoginToken());
        if (ObjectUtil.isNull(user)) {
            throw new SecurityException(HttpStatus.UNAUTHORIZED);
        }
        log.info("User login: {}", user);
        val matched = bCryptPasswordEncoder.matches(payload.getPassword(), user.getPassword());
        if (!matched) {
            throw new SecurityException(HttpStatus.UNAUTHORIZED);
        }
        val jwt = jwtService.createJwt(payload.getRememberMe(), user.getId(), user.getUsername(), null, null);
        val response = new LoginResponse();
        response.setGreeting(messageSource.getMessage(("greeting"), null, LocaleContextHolder.getLocale()));
        response.setJwt(jwt);
        return response;
    }

    @Override
    public boolean logout(HttpServletRequest request) throws SecurityException {
        jwtService.invalidateJwt(request);
        return true;
    }

    @Override
    public String getUserStatus(@Valid @NotNull GetUserStatusPayload payload) {
        final ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        log.info("getHeader: {}", servletRequestAttributes.getRequest().getHeader("X-Username"));
        return UserStatus.ofValue(payload.getStatus()).getDescription();
    }
}
