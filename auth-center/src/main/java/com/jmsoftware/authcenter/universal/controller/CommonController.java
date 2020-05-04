package com.jmsoftware.authcenter.universal.controller;

import com.jmsoftware.authcenter.universal.domain.ValidationTestPayload;
import com.jmsoftware.authcenter.universal.service.CommonService;
import com.jmsoftware.authcenter.universal.service.RedisService;
import com.jmsoftware.common.bean.ResponseBodyBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
@Api(tags = {"Common Controller"})
public class CommonController {
    private final CommonService commonService;
    private final RedisService redisService;

    @GetMapping("/app-info")
    @ApiOperation(value = "/app-info", notes = "Retrieve application information")
    public ResponseBodyBean<Map<String, Object>> applicationInformation() {
        var data = commonService.getApplicationInfo();
        redisService.set("appInfo", data.toString());
        return ResponseBodyBean.ofDataAndMessage(data, "Succeed to retrieve app info.");
    }

    @PostMapping("/validation-test")
    @ApiOperation(value = "/validation-test", notes = "Validation of request payload test")
    public ResponseBodyBean<String> validationTest(@RequestBody ValidationTestPayload payload) {
        commonService.validateObject(payload);
        return ResponseBodyBean.ofDataAndMessage(payload.getName(), "validationTest()");
    }
}