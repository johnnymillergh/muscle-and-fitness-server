package com.jmsoftware.authcenter.universal.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.authcenter.universal.domain.RolePO;
import com.jmsoftware.authcenter.universal.mapper.RoleMapper;
import com.jmsoftware.authcenter.universal.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>RoleServiceImpl</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-05-18 12:03
 **/
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;

    @Override
    public List<RolePO> getList(Page page) {
        return roleMapper.selectPageList(page);
    }

    @Override
    public boolean checkRoleName(RolePO po) {
        return roleMapper.checkRoleName(po) == 0;
    }

    @Override
    public boolean insertRole(RolePO po) {
        return roleMapper.insertRole(po) > 0;
    }

    @Override
    public String handleRoleName(String roleName) {
        String processedRoleName = StrUtil.trim(roleName).toLowerCase();
        return processedRoleName.replaceAll("\\s", "_");
    }

    @Override
    public RolePO searchRole(String roleName) {
        return roleMapper.selectRoleByName(roleName);
    }

    @Override
    public boolean updateRole(RolePO po) {
        return roleMapper.updateRoleById(po) == 1;
    }

    @Override
    public List<RolePO> getRolesByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    public List<RolePO> getListForSelection(Page page) {
        return roleMapper.selectRoleListForSelection(page).getRecords();
    }
}
