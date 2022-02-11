package com.jmsoftware.maf.authcenter.user.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.authcenter.user.constant.UserRedisKey;
import com.jmsoftware.maf.authcenter.user.converter.UserMapStructMapper;
import com.jmsoftware.maf.authcenter.user.mapper.UserMapper;
import com.jmsoftware.maf.authcenter.user.payload.GetUserPageListPayload;
import com.jmsoftware.maf.authcenter.user.payload.GetUserStatusPayload;
import com.jmsoftware.maf.authcenter.user.persistence.User;
import com.jmsoftware.maf.authcenter.user.service.UserRoleService;
import com.jmsoftware.maf.authcenter.user.service.UserService;
import com.jmsoftware.maf.common.bean.PageResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.user.*;
import com.jmsoftware.maf.common.exception.SecurityException;
import com.jmsoftware.maf.springcloudstarter.property.MafConfigurationProperties;
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties;
import com.jmsoftware.maf.springcloudstarter.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static java.util.concurrent.TimeUnit.DAYS;

/**
 * <h1>UserServiceImpl</h1>
 * <p>
 * Service implementation of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊), date 2020-05-10 12:08:28
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user-service-cache")
public class UserServiceImpl
        extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final MessageSource messageSource;
    private final MafProjectProperties mafProjectProperties;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRoleService userRoleService;
    private final MafConfigurationProperties mafConfigurationProperties;

    @Override
    public GetUserByLoginTokenResponse getUserByLoginToken(@NotBlank String loginToken) {
        val key = String.format(String.format("%s%s", this.mafProjectProperties.getProjectParentArtifactId(),
                                              UserRedisKey.GET_USER_BY_LOGIN_TOKEN.getKeyInfixFormat()), loginToken);
        val hasKey = this.redisTemplate.hasKey(key);
        if (BooleanUtil.isTrue(hasKey)) {
            return JSONUtil.toBean(this.redisTemplate.opsForValue().get(key), GetUserByLoginTokenResponse.class);
        }
        val wrapper = Wrappers.lambdaQuery(User.class);
        wrapper.and(queryWrapper -> queryWrapper.eq(User::getUsername, loginToken)
                .or()
                .eq(User::getEmail, loginToken)
                .or()
                .eq(User::getCellphone, loginToken));
        val user = this.getBaseMapper().selectOne(wrapper);
        if (ObjectUtil.isNull(user)) {
            return null;
        }
        val response = UserMapStructMapper.INSTANCE.of(user);
        this.redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(response), RandomUtil.randomLong(1, 7), DAYS);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public SignupResponse saveUserForSignup(@Valid SignupPayload payload) {
        val user = new User();
        user.setUsername(payload.getUsername());
        user.setEmail(payload.getEmail());
        user.setPassword(this.bCryptPasswordEncoder.encode(payload.getPassword()));
        user.setStatus(UserStatus.ENABLED.getValue());
        this.save(user);
        log.warn("Saved user for signup, going to assign guest role to user. {}", user);
        this.userRoleService.assignRoleByRoleName(user, this.mafConfigurationProperties.getGuestUserRole());
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
        val matched = this.bCryptPasswordEncoder.matches(payload.getPassword(), user.getPassword());
        if (!matched) {
            throw new SecurityException(HttpStatus.UNAUTHORIZED);
        }
        val jwt = this.jwtService.createJwt(payload.getRememberMe(), user.getId(), user.getUsername(), null, null);
        val response = new LoginResponse();
        response.setGreeting(this.messageSource.getMessage("greeting", null, LocaleContextHolder.getLocale()));
        response.setJwt(jwt);
        return response;
    }

    @Override
    public boolean logout(HttpServletRequest request) throws SecurityException {
        this.jwtService.invalidateJwt(request);
        return true;
    }

    @Override
    public String getUserStatus(@Valid @NotNull GetUserStatusPayload payload) {
        log.info("Current username: {}", UserUtil.getCurrentUsername());
        return UserStatus.ofValue(payload.getStatus()).getDescription();
    }

    @Override
    public PageResponseBodyBean<User> getUserPageList(@Valid @NotNull GetUserPageListPayload payload) {
        log.info("{}", payload);
        val page = new Page<User>(payload.getCurrentPage(), payload.getPageSize());
        val queryWrapper = Wrappers.lambdaQuery(User.class);
        if (CharSequenceUtil.isNotBlank(payload.getUsername())) {
            queryWrapper.like(User::getUsername, payload.getUsername());
        }
        page.setOrders(ListUtil.of(OrderItem.desc(payload.getOrderBy())));
        this.page(page, queryWrapper);
        return PageResponseBodyBean.ofSuccess(page.getRecords(), page.getTotal());
    }
}
