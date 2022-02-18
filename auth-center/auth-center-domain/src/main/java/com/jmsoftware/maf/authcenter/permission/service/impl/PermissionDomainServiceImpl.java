package com.jmsoftware.maf.authcenter.permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmsoftware.maf.authcenter.permission.mapper.PermissionMapper;
import com.jmsoftware.maf.authcenter.permission.persistence.Permission;
import com.jmsoftware.maf.authcenter.permission.service.PermissionDomainService;
import com.jmsoftware.maf.common.domain.authcenter.permission.PermissionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <h1>PermissionDomainServiceImpl</h1>
 * <p>
 * Service implementation of Permission.(Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 5/11/20 8:34 AM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionDomainServiceImpl
        extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionDomainService {
    @Override
    public List<Permission> getPermissionListByRoleIdList(
            @NotEmpty List<Long> roleIdList,
            @NotEmpty List<PermissionType> permissionTypeList
    ) {
        return this.getBaseMapper().selectPermissionListByRoleIdList(roleIdList, permissionTypeList);
    }
}
