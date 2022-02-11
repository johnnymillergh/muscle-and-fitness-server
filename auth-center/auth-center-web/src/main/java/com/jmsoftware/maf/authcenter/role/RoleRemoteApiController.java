package com.jmsoftware.maf.authcenter.role;

import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.role.GetRoleListByUserIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>RoleRemoteApiController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 5/10/20 10:43 PM
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/role-remote-api")
public class RoleRemoteApiController {
    private final RoleDomainService roleDomainService;

    @GetMapping("/roles/{userId}")
    public ResponseBodyBean<GetRoleListByUserIdResponse> getRoleList(@PathVariable Long userId) {
        return ResponseBodyBean.ofSuccess(this.roleDomainService.getRoleList(userId));
    }
}
