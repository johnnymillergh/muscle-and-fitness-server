package com.jmsoftware.maf.reactivespringcloudstarter.controller;

import cn.hutool.json.JSON;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.ValidationTestPayload;
import com.jmsoftware.maf.reactivespringcloudstarter.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>CommonController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/4/20 10:29 AM
 **/
@Slf4j
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
