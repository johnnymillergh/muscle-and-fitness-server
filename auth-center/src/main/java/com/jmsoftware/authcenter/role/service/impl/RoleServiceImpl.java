package com.jmsoftware.authcenter.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.jmsoftware.authcenter.role.entity.RolePersistence;
import com.jmsoftware.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.authcenter.role.service.RoleService;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdPayload;
import com.jmsoftware.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <h1>RoleServiceImpl</h1>
 * <p>
 * Service implementation of Role.(Role)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020-05-10 22:39:50
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public RolePersistence queryById(Long id) {
        return this.roleMapper.queryById(id);
    }

    @Override
    public GetRoleListByUserIdResponse getRoleListByUserId(GetRoleListByUserIdPayload payload) {
        val roleList = this.getRoleListByUserId(payload.getUserId());
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
        return roleMapper.selectRoleListByUserId(userId);
    }

    @Override
    public List<RolePersistence> queryAllByLimit(int offset, int limit) {
        return this.roleMapper.queryAllByLimit(offset, limit);
    }

    @Override
    public RolePersistence insert(RolePersistence rolePersistence) {
        this.roleMapper.insert(rolePersistence);
        return rolePersistence;
    }

    @Override
    public RolePersistence update(RolePersistence rolePersistence) {
        this.roleMapper.update(rolePersistence);
        return this.queryById(rolePersistence.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.roleMapper.deleteById(id) > 0;
    }
}
