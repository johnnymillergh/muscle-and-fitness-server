package com.jmsoftware.maf.authcenter.permission.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.maf.authcenter.permission.mapper.PermissionMapper;
import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByRoleIdListResponse;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdPayload;
import com.jmsoftware.maf.common.domain.authcenter.permission.GetPermissionListByUserIdResponse;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * <h1>PermissionServiceImpl</h1>
 * <p>
 * Service implementation of Permission.(Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionPersistence> implements PermissionService {
    @Override
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
        return this.getBaseMapper().selectPermissionListByRoleIdList(roleIdList);
    }

    @Override
    public GetPermissionListByUserIdResponse getPermissionListByUserId(@Valid GetPermissionListByUserIdPayload payload) {
        val permissionList = this.getPermissionListByUserId(payload.getUserId());
        val response = new GetPermissionListByUserIdResponse();
        permissionList.forEach(permissionPersistence -> {
            val permission = new GetPermissionListByUserIdResponse.Permission();
            BeanUtil.copyProperties(permissionPersistence, permission);
            response.getPermissionList().add(permission);
        });
        return response;
    }

    @Override
    public List<PermissionPersistence> getPermissionListByUserId(@NonNull Long userId) {
        return this.getBaseMapper().selectPermissionListByUserId(userId);
    }
}
