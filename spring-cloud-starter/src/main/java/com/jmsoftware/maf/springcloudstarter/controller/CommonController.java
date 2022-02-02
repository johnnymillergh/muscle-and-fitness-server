package com.jmsoftware.maf.springcloudstarter.controller;

import cn.hutool.json.JSON;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.ValidationTestPayload;
import com.jmsoftware.maf.springcloudstarter.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>CommonController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 1:45 PM
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {
    private final CommonService commonService;

    @GetMapping("/app-info")
    public ResponseBodyBean<JSON> applicationInformation() {
        return ResponseBodyBean.ofSuccess(this.commonService.getApplicationInfo(), "Succeed to retrieve app info.");
    }

    @PostMapping("/validation-test")
    public ResponseBodyBean<String> validationTest(@RequestBody ValidationTestPayload payload) {
        this.commonService.validateObject(payload);
        return ResponseBodyBean.ofSuccess(payload.getName(), "validationTest()");
    }
}
