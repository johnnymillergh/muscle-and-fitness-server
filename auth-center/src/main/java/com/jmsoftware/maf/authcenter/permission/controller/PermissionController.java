package com.jmsoftware.maf.authcenter.permission.controller;

import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>PermissionController</h1>
 * <p>
 * Controller implementation of Permission.(Permission)
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@RestController
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;
}
