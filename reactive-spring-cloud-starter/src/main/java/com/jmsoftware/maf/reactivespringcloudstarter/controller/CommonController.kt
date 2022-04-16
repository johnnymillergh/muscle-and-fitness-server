package com.jmsoftware.maf.reactivespringcloudstarter.controller

import cn.hutool.json.JSON
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.ValidationTestPayload
import com.jmsoftware.maf.reactivespringcloudstarter.service.CommonService
import org.springframework.web.bind.annotation.*

/**
 * # CommonController
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 11:37 AM
 */
@RestController
@RequestMapping("/common")
class CommonController(
    private val commonService: CommonService
) {
    @GetMapping("/app-info")
    fun applicationInformation(): ResponseBodyBean<JSON> {
        return ResponseBodyBean.ofSuccess(commonService.applicationInfo, "Succeed to retrieve app info.")
    }

    @PostMapping("/validation-test")
    fun validationTest(@RequestBody payload: ValidationTestPayload): ResponseBodyBean<String> {
        commonService.validateObject(payload)
        return ResponseBodyBean.ofSuccess(payload.name, "validationTest()")
    }
}
