package com.jmsoftware.authcenter.permission.controller;

import com.jmsoftware.authcenter.permission.entity.PermissionPersistence;
import com.jmsoftware.authcenter.permission.service.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

 /**
 * <h1>PermissionController</h1>
 * <p>
 * Controller implementation of Permission.(Permission)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@RestController
@RequestMapping("permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    @GetMapping("selectOne")
    public PermissionPersistence selectOne(Long id) {
        return this.permissionService.queryById(id);
    }
}
