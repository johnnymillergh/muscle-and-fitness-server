package com.jmsoftware.apiportal.universal.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jmsoftware.apiportal.universal.domain.UserPO;
import com.jmsoftware.apiportal.universal.mapper.UserMapper;
import com.jmsoftware.apiportal.universal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h1>AuthServiceImpl</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-28 20:25
 **/
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;

    @Override
    public boolean checkUsernameUniqueness(String username) {
        Integer count = userMapper.countByUsername(username);
        return count == 0;
    }

    @Override
    public boolean checkEmailUniqueness(String email) {
        Integer count = userMapper.countByEmail(email);
        return count == 0;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserPO register(UserPO po) {
        userMapper.register(po);
        return po;
    }

    @Override
    public boolean validateUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return false;
        }
        Integer countByUsername = userMapper.countByUsername(username);
        return countByUsername != 0;
    }
}
