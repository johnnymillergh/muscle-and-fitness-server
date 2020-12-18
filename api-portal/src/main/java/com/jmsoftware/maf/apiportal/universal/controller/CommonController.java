package com.jmsoftware.maf.apiportal.universal.controller;

import com.jmsoftware.maf.apiportal.universal.domain.ValidationTestPayload;
import com.jmsoftware.maf.apiportal.universal.service.CommonService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <h1>CommonController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/4/20 10:29 AM
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
@Api(tags = {"Common Controller"})
public class CommonController {
    private final CommonService commonService;

    @GetMapping("/app-info")
    @ApiOperation(value = "/app-info", notes = "Retrieve application information")
    public ResponseBodyBean<Map<String, Object>> applicationInformation() {
        val data = commonService.getApplicationInfo();
        return ResponseBodyBean.ofSuccess(data, "Succeed to retrieve app info.");
    }

    @PostMapping("/validation-test")
    @ApiOperation(value = "/validation-test", notes = "Validation of request payload test")
    public ResponseBodyBean<String> validationTest(@RequestBody ValidationTestPayload payload) {
        commonService.validateObject(payload);
        return ResponseBodyBean.ofSuccess(payload.getName(), "validationTest()");
    }

    @GetMapping("/get-jwt")
    @ApiOperation(value = "/get-jwt", notes = "Get JWT")
    public ResponseBodyBean<String> getJwt(String username) {
        return ResponseBodyBean.ofSuccess(commonService.generateJwt(username));
    }
}
