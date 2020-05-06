package com.jmsoftware.apiportal.universal.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.apiportal.universal.domain.ApiStatus;
import com.jmsoftware.apiportal.universal.domain.GetApiListPLO;
import com.jmsoftware.apiportal.universal.domain.PermissionPO;
import com.jmsoftware.apiportal.universal.mapper.PermissionMapper;
import com.jmsoftware.apiportal.universal.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>PermissionServiceImpl</h1>
 * <p>Change description here</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-05-10 20:46
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionMapper permissionMapper;

    @Override
    public boolean savePermission(PermissionPO po) {
        return permissionMapper.insertPermission(po) > 0;
    }

    @Override
    public List<PermissionPO> selectByRoleIdList(List<Long> ids) {
        return permissionMapper.selectByRoleIdList(ids);
    }

    @Override
    public ApiStatus checkApiIsInUse(String url) {
        return permissionMapper.countInUseApiByUrl(url) == 1 ? ApiStatus.IN_USE : ApiStatus.IDLED;
    }

    @Override
    public List<PermissionPO> selectApisByUrlPrefix(String urlPrefix) {
        return permissionMapper.selectApisByUrlPrefix(urlPrefix);
    }

    @Override
    public List<PermissionPO> queryApiList(GetApiListPLO plo) {
        return permissionMapper.selectApiPageList(new Page(plo.getCurrentPage(), plo.getPageSize())).getRecords();
    }
}
