package com.jmsoftware.maf.authcenter.role.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.authcenter.role.entity.constant.RoleRedisKey;
import com.jmsoftware.maf.authcenter.role.entity.persistence.Role;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdSingleResponse;
import com.jmsoftware.maf.springcloudstarter.configuration.MafConfiguration;
import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <h1>RoleServiceImpl</h1>
 * <p>
 * Service implementation of Role.(Role)
 *
 * @author Johnny Miller (锺俊)
 * @date 2020-05-10 22:39:50
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final MafProjectProperty mafProjectProperty;
    private final MafConfiguration mafConfiguration;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows({JsonProcessingException.class})
    public GetRoleListByUserIdResponse getRoleList(@NotNull Long userId) {
        val key = String.format(mafProjectProperty.getProjectParentArtifactId()
                                        + RoleRedisKey.GET_ROLE_LIST_BY_USER_ID.getKeyInfixFormat(), userId);
        val hasKey = redisTemplate.hasKey(key);
        if (BooleanUtil.isTrue(hasKey)) {
            return objectMapper.readValue(redisTemplate.opsForValue().get(key), GetRoleListByUserIdResponse.class);
        }
        val response = new GetRoleListByUserIdResponse();
        response.setRoleList(this.getRoleListByUserId(userId));
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(response), RandomUtil.randomLong(1, 7),
                                        TimeUnit.DAYS);
        return response;
    }

    @Override
    public List<GetRoleListByUserIdSingleResponse> getRoleListByUserId(@NonNull Long userId) {
        return this.getBaseMapper().selectRoleListByUserId(userId);
    }

    @Override
    public boolean checkAdmin(@NotEmpty List<@NotNull Long> roleIdList) {
        val wrapper = Wrappers.lambdaQuery(Role.class);
        wrapper.select(Role::getName)
                .in(Role::getId, roleIdList);
        val roleList = this.list(wrapper);
        val roleNameSet = roleList
                .stream()
                .map(Role::getName)
                .filter(roleName -> StrUtil.equals(mafConfiguration.getSuperUserRole(), roleName))
                .collect(Collectors.toSet());
        // If roleNameSet is not empty (contains "admin")
        return CollUtil.isNotEmpty(roleNameSet);
    }
}
