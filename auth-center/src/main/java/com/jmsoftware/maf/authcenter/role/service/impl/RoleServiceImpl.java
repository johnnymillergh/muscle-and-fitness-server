package com.jmsoftware.maf.authcenter.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.role.entity.RolePersistence;
import com.jmsoftware.maf.authcenter.role.mapper.RoleMapper;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>RoleServiceImpl</h1>
 * <p>
 * Service implementation of Role.(Role)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020-05-10 22:39:50
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePersistence> implements RoleService {
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
}
