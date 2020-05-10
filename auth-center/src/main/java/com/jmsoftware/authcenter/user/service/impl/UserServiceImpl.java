package com.jmsoftware.authcenter.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.authcenter.user.entity.UserPersistence;
import com.jmsoftware.authcenter.user.mapper.UserMapper;
import com.jmsoftware.authcenter.user.service.UserService;
import com.jmsoftware.common.domain.authcenter.user.GetUserByLoginTokenResponse;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <h1>UserServiceImpl</h1>
 * <p>
 * Service implementation of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020-05-10 12:08:28
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserPersistence queryById(Long id) {
        return this.userMapper.selectById(id);
    }

    @Override
    public List<UserPersistence> queryAllByLimit(int offset, int limit) {
        return this.userMapper.selectAllByLimit(offset, limit);
    }

    @Override
    public UserPersistence insert(UserPersistence userPersistence) {
        this.userMapper.insert(userPersistence);
        return userPersistence;
    }

    @Override
    public UserPersistence update(UserPersistence userPersistence) {
        this.userMapper.update(userPersistence);
        return this.queryById(userPersistence.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.userMapper.deleteById(id) > 0;
    }

    @Override
    public GetUserByLoginTokenResponse getUserByLoginToken(String loginToken) {
        if (StrUtil.isBlank(loginToken)) {
            return null;
        }
        val userPersistence = userMapper.selectUserByUsernameOrEmail(loginToken);
        if (ObjectUtil.isNull(userPersistence)) {
            return null;
        }
        val response = new GetUserByLoginTokenResponse();
        BeanUtil.copyProperties(userPersistence, response);
        return response;
    }
}
