package com.jmsoftware.maf.authcenter.role.controller;

import com.jmsoftware.maf.authcenter.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: RoleController, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/17/2020 4:44 PM
 **/
@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
}
