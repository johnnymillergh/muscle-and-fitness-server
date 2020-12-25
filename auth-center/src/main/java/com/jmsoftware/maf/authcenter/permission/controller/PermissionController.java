package com.jmsoftware.maf.authcenter.permission.controller;

import com.jmsoftware.maf.authcenter.permission.service.PermissionService;
import com.jmsoftware.maf.authcenter.universal.configuration.ProjectProperty;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springbootstarter.helper.HttpApiScanHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

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
@Api(tags = {"Permission API"})
public class PermissionController {
    private final PermissionService permissionService;
    private final DiscoveryClient discoveryClient;
    private final HttpApiScanHelper httpApiScanHelper;
    private final ProjectProperty projectProperty;

    @GetMapping("/permissions/services-info")
    @ApiOperation(value = "Get services info", notes = "Get services info")
    public ResponseBodyBean<?> servicesInfo() {
        List<String> services = discoveryClient.getServices();
        log.info("Service ID: {}", services);
        val resultMap = new LinkedHashMap<String, Object>();
        services.forEach(service -> {
            val instances = discoveryClient.getInstances(service);
            log.info("Instances: {}", instances);
            resultMap.put(service, instances);
        });
        val httpApiMap = httpApiScanHelper.scan(projectProperty.getBasePackage());
        resultMap.put("httpApiMap", httpApiMap.toString());
        return ResponseBodyBean.ofSuccess(resultMap);
    }
}
