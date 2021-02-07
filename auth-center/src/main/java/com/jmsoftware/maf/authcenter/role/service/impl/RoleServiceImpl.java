package com.jmsoftware.maf.authcenter.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.role.entity.RolePersistence;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import com.jmsoftware.maf.springcloudstarter.configuration.MafConfiguration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePersistence> implements RoleService {
    private final MafConfiguration mafConfiguration;

    @Override
    public GetRoleListByUserIdResponse getRoleList(Long userId) {
        val roleList = this.getRoleListByUserId(userId);
        GetRoleListByUserIdResponse response = new GetRoleListByUserIdResponse();
        roleList.forEach(rolePersistence -> {
            GetRoleListByUserIdResponse.Role role = new GetRoleListByUserIdResponse.Role();
            BeanUtil.copyProperties(rolePersistence, role);
            response.getRoleList().add(role);
        });
        return response;
    }

    @Override
    public List<RolePersistence> getRoleListByUserId(@NonNull Long userId) {
        return this.getBaseMapper().selectRoleListByUserId(userId);
    }

    @Override
    public boolean checkAdmin(@NotEmpty List<@NotNull Long> roleIdList) {
        LambdaQueryWrapper<RolePersistence> wrapper = Wrappers.lambdaQuery();
        wrapper.select(RolePersistence::getName)
                .in(RolePersistence::getId, roleIdList);
        val rolePersistenceList = this.list(wrapper);
        val roleNameSet = rolePersistenceList
                .stream()
                .map(RolePersistence::getName)
                .filter(roleName -> StrUtil.equals(mafConfiguration.getSuperUserRole(), roleName))
                .collect(Collectors.toSet());
        // If roleNameSet is not empty (contains "admin")
        return CollUtil.isNotEmpty(roleNameSet);
    }
}
