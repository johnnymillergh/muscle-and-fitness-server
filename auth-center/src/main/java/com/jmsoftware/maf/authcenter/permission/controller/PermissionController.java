package com.jmsoftware.maf.authcenter.permission.controller;

import com.jmsoftware.maf.authcenter.permission.entity.GetServicesInfoResponse;
import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>PermissionController</h1>
 * <p>
 * Controller implementation of Permission.(Permission)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com
 * @date 5/11/20 8:34 AM
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    /**
     * Services info response body bean.
     *
     * @return the response body bean
     * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/25/2020 5:44 PM
     * @see
     * <a href='https://github.com/spring-cloud/spring-cloud-netflix/issues/990#issuecomment-214943106'>RestTemplate Excample</a>
     */
    @GetMapping("/permissions/services-info")
    public ResponseBodyBean<GetServicesInfoResponse> getServicesInfo() throws BizException {
        return ResponseBodyBean.ofSuccess(this.permissionService.getServicesInfo());
    }
}
