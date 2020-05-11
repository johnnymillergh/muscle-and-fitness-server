package com.jmsoftware.authcenter.permission.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.jmsoftware.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.authcenter.permission.mapper.PermissionMapper;
import com.jmsoftware.authcenter.permission.service.PermissionService;
import com.jmsoftware.authcenter.universal.aspect.ValidateArgument;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * <h1>PermissionServiceImpl</h1>
 * <p>
 * Service implementation of Permission.(Permission)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public PermissionPersistence queryById(Long id) {
        return this.permissionMapper.queryById(id);
    }

    @Override
    public List<PermissionPersistence> queryAllByLimit(int offset, int limit) {
        return this.permissionMapper.queryAllByLimit(offset, limit);
    }

    @Override
    public PermissionPersistence insert(PermissionPersistence permissionPersistence) {
        this.permissionMapper.insert(permissionPersistence);
        return permissionPersistence;
    }

    @Override
    public PermissionPersistence update(PermissionPersistence permissionPersistence) {
        this.permissionMapper.update(permissionPersistence);
        return this.queryById(permissionPersistence.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.permissionMapper.deleteById(id) > 0;
    }

    @Override
    @ValidateArgument
    public GetPermissionListByRoleIdListResponse getPermissionListByRoleIdList(@Valid GetPermissionListByRoleIdListPayload payload) {
        val permissionList = this.getPermissionListByRoleIdList(payload.getRoleIdList());
        val response = new GetPermissionListByRoleIdListResponse();
        permissionList.forEach(permissionPersistence -> {
            GetPermissionListByRoleIdListResponse.Permission permission =
                    new GetPermissionListByRoleIdListResponse.Permission();
            BeanUtil.copyProperties(permissionPersistence, permission);
            response.getPermissionList().add(permission);
        });
        return response;
    }

    @Override
    public List<PermissionPersistence> getPermissionListByRoleIdList(@NonNull List<Long> roleIdList) {
        if (CollUtil.isEmpty(roleIdList)) {
            return Collections.emptyList();
        }
        return permissionMapper.selectPermissionListByRoleIdList(roleIdList);
    }
}
