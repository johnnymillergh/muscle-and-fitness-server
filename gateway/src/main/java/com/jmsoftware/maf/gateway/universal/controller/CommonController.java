package com.jmsoftware.maf.gateway.universal.controller;

import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.gateway.remoteapi.AuthCenterRemoteApi;
import com.jmsoftware.maf.gateway.universal.domain.ValidationTestPayload;
import com.jmsoftware.maf.gateway.universal.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <h1>CommonController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/4/20 10:29 AM
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
@Api(tags = {"Common Controller"})
public class CommonController {
    private final CommonService commonService;
    private final AuthCenterRemoteApi authCenterRemoteApi;

    @GetMapping("/app-info")
    @ApiOperation(value = "/app-info", notes = "Retrieve application information")
    public ResponseBodyBean<Map<String, Object>> applicationInformation() {
        val data = commonService.getApplicationInfo();
        final var roleListByUserId = authCenterRemoteApi.getRoleListByUserId(1L);
        roleListByUserId
                .map(getRoleListByUserIdResponseResponseBodyBean -> {
                    log.info("Response1: {}", getRoleListByUserIdResponseResponseBodyBean);
                    return getRoleListByUserIdResponseResponseBodyBean.getData();
                })
                .doOnNext(getRoleListByUserIdResponse -> log.info("Response2: {}", getRoleListByUserIdResponse.getRoleList()))
                .subscribe();
        return ResponseBodyBean.ofSuccess(data, "Succeed to retrieve app info.");
    }

    @PostMapping("/validation-test")
    @ApiOperation(value = "/validation-test", notes = "Validation of request payload test")
    public ResponseBodyBean<String> validationTest(@RequestBody ValidationTestPayload payload) {
        commonService.validateObject(payload);
        return ResponseBodyBean.ofSuccess(payload.getName(), "validationTest()");
    }
}
