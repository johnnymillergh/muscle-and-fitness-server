package com.jmsoftware.maf.authcenter.role.controller;

import com.jmsoftware.maf.authcenter.role.entity.RolePersistence;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

 /**
 * <h1>RoleController</h1>
 * <p>
 * Controller implementation of Role.(Role)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020-05-10 22:39:51
 */
@RestController
@RequestMapping("role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("selectOne")
    public RolePersistence selectOne(Long id) {
        return this.roleService.queryById(id);
    }
}
